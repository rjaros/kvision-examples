package com.example

import com.github.andrewoma.kwery.core.builder.query
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.await
import org.springframework.data.r2dbc.core.awaitOne
import org.springframework.data.r2dbc.core.awaitOneOrNull
import org.springframework.data.r2dbc.core.flow
import org.springframework.data.r2dbc.mapping.SettableValue
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import pl.treksoft.kvision.types.OffsetDateTime

interface WithProfile {
    val serverRequest: ServerRequest

    suspend fun getProfile(): Profile {
        return serverRequest.principal().ofType(Authentication::class.java).map {
            it.principal as Profile
        }.awaitSingle()
    }
}

fun DatabaseClient.GenericExecuteSpec.bindMap(parameters: Map<String, Any?>): DatabaseClient.GenericExecuteSpec {
    return parameters.entries.fold(this) { spec, entry ->
        if (entry.value == null) {
            spec.bindNull(entry.key, String::class.java)
        } else {
            spec.bind(entry.key, SettableValue.fromOrEmpty(entry.value, entry.value!!::class.java))
        }
    }
}

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
actual class AddressService(override val serverRequest: ServerRequest, private val databaseClient: DatabaseClient) :
    IAddressService, WithProfile {

    override suspend fun getAddressList(search: String?, types: String, sort: Sort): List<Address> {
        val profile = getProfile()
        val query = query {
            select("SELECT * FROM address")
            whereGroup {
                where("user_id = :user_id")
                parameter("user_id", profile.id?.toInt())
                search?.let {
                    where(
                        """(lower(first_name) like :search
                            OR lower(last_name) like :search
                            OR lower(email) like :search
                            OR lower(phone) like :search
                            OR lower(postal_address) like :search)""".trimMargin()
                    )
                    parameter("search", "%${it.toLowerCase()}%")
                }
                if (types == "fav") {
                    where("favourite")
                }
            }
            when (sort) {
                Sort.FN -> orderBy("lower(first_name)")
                Sort.LN -> orderBy("lower(last_name)")
                Sort.E -> orderBy("lower(email)")
                Sort.F -> orderBy("favourite")
            }
        }
        return databaseClient.execute(query.sql).bindMap(query.parameters).`as`(Address::class.java).fetch().flow()
            .toList()
    }

    override suspend fun addAddress(address: Address): Address {
        val profile = getProfile()
        val newAddress = address.copy(id = null, userId = profile.id?.toInt(), createdAt = OffsetDateTime.now())
        val id = databaseClient.insert().into(Address::class.java).using(newAddress)
            .map { row -> row.get("id", java.lang.Integer::class.java) }.awaitOne()
        return newAddress.copy(id = id?.toInt())
    }

    override suspend fun updateAddress(address: Address): Address {
        val profile = getProfile()
        address.id?.let { id ->
            databaseClient.select().from(Address::class.java).matching(
                where("id").`is`(id).and("user_id").`is`(
                    profile.id?.toInt() ?: 0
                )
            ).fetch().awaitOneOrNull()?.let { oldAddress ->
                val newAddress = address.copy(userId = profile.id?.toInt(), createdAt = oldAddress.createdAt)
                databaseClient.update().table(Address::class.java)
                    .using(newAddress).fetch().rowsUpdated().awaitSingle()
                return newAddress
            } ?: throw IllegalArgumentException("Address not found")
        } ?: throw IllegalArgumentException("The ID of the address is not set")
    }

    override suspend fun deleteAddress(id: Int): Boolean {
        return databaseClient.delete().from(Address::class.java)
            .matching(where("id").`is`(id)).fetch().rowsUpdated().awaitSingle() == 1
    }
}

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
actual class ProfileService(override val serverRequest: ServerRequest) : IProfileService, WithProfile {
    override suspend fun getProfile(): Profile {
        return super.getProfile()
    }
}

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
actual class RegisterProfileService(
    private val databaseClient: DatabaseClient,
    private val passwordEncoder: PasswordEncoder
) : IRegisterProfileService {

    override suspend fun registerProfile(profile: Profile, password: String): Boolean {
        try {
            databaseClient.insert().into(User::class.java).using(
                User(
                    username = profile.username!!,
                    name = profile.name!!,
                    password = passwordEncoder.encode(password)
                )
            ).await()
        } catch (e: Exception) {
            throw Exception("Register operation failed!")
        }
        return true
    }
}
