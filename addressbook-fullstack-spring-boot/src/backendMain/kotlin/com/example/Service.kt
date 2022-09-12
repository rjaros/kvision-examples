package com.example

import io.kvision.types.OffsetDateTime
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.data.r2dbc.core.allAndAwait
import org.springframework.data.r2dbc.core.awaitOneOrNull
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.r2dbc.core.flow
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import pl.treksoft.e4k.core.DbClient
import pl.treksoft.e4k.core.delete
import pl.treksoft.e4k.core.execute
import pl.treksoft.e4k.core.insert
import pl.treksoft.e4k.core.table
import pl.treksoft.e4k.core.update
import pl.treksoft.e4k.core.using
import pl.treksoft.e4k.query.parameterNullable
import pl.treksoft.e4k.query.query

interface WithProfile {
    val serverRequest: ServerRequest

    suspend fun getProfile(): Profile {
        return serverRequest.principal().ofType(Authentication::class.java).map {
            it.principal as Profile
        }.awaitSingle()
    }
}

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class AddressService(override val serverRequest: ServerRequest, private val dbClient: DbClient) :
    IAddressService, WithProfile {

    override suspend fun getAddressList(search: String?, types: String, sort: Sort): List<Address> {
        val profile = getProfile()
        val query = query {
            select("SELECT * FROM address")
            whereGroup {
                where("user_id = :user_id")
                parameterNullable("user_id", profile.id?.toInt())
                search?.let {
                    where(
                        """(lower(first_name) like :search
                            OR lower(last_name) like :search
                            OR lower(email) like :search
                            OR lower(phone) like :search
                            OR lower(postal_address) like :search)""".trimMargin()
                    )
                    parameter("search", "%${it.lowercase()}%")
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
        return dbClient.execute<Address>(query).flow().toList()
    }

    override suspend fun addAddress(address: Address): Address {
        val profile = getProfile()
        val newAddress = address.copy(id = null, userId = profile.id?.toInt(), createdAt = OffsetDateTime.now())
        val id = dbClient.r2dbcEntityTemplate.insert(Address::class.java).using(newAddress)
            .map { row -> row.id }.awaitSingle()
        return newAddress.copy(id = id)
    }

    override suspend fun updateAddress(address: Address): Address {
        val profile = getProfile()
        address.id?.let { id ->
            dbClient.r2dbcEntityTemplate.select(Address::class.java).matching(
                query(
                    where("id").`is`(id).and("user_id").`is`(
                        profile.id?.toInt() ?: 0
                    )
                )
            ).awaitOneOrNull()?.let { oldAddress ->
                val newAddress = address.copy(userId = profile.id?.toInt(), createdAt = oldAddress.createdAt)
                dbClient.update().table<Address>().using(newAddress, dbClient).awaitSingle()
                return newAddress
            } ?: throw IllegalArgumentException("Address not found")
        } ?: throw IllegalArgumentException("The ID of the address is not set")
    }

    override suspend fun deleteAddress(id: Int): Boolean {
        return dbClient.delete().from(Address::class.java)
            .matching(query(where("id").`is`(id))).allAndAwait() == 1
    }
}

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class ProfileService(override val serverRequest: ServerRequest) : IProfileService, WithProfile {
    override suspend fun getProfile(): Profile {
        return super.getProfile()
    }
}

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class RegisterProfileService(
    private val dbClient: DbClient,
    private val passwordEncoder: PasswordEncoder
) : IRegisterProfileService {

    override suspend fun registerProfile(profile: Profile, password: String): Boolean {
        try {
            dbClient.insert().into(User::class.java).using(
                User(
                    username = profile.username!!,
                    name = profile.name!!,
                    password = passwordEncoder.encode(password)
                )
            ).awaitSingle()
        } catch (e: Exception) {
            throw Exception("Register operation failed!")
        }
        return true
    }
}
