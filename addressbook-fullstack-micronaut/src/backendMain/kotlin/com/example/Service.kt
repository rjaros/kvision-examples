package com.example

import com.github.andrewoma.kwery.core.builder.query
import io.kvision.types.OffsetDateTime
import io.micronaut.context.annotation.Prototype
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.filters.SecurityFilter
import io.micronaut.session.http.SessionForRequest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.await
import org.springframework.data.r2dbc.core.awaitOne
import org.springframework.data.r2dbc.core.awaitOneOrNull
import org.springframework.data.r2dbc.core.flow
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.security.crypto.password.PasswordEncoder

interface WithProfile {
    val httpRequest: HttpRequest<*>
    val databaseClient: DatabaseClient

    suspend fun getUser(): User {
        return SessionForRequest.find(httpRequest).orElse(null)?.let { session ->
            session.get(SecurityFilter.AUTHENTICATION, Authentication::class.java).orElse(null)?.let { authentication ->
                (authentication).name?.let { username ->
                    databaseClient.select().from(User::class.java)
                        .matching(
                            where("username").`is`(username)
                        ).fetch().first().awaitSingle()
                }
            }
        } ?: throw Exception("Register operation failed!")
    }
}

@Prototype
actual class AddressService(override val httpRequest: HttpRequest<*>, override val databaseClient: DatabaseClient) :
    IAddressService, WithProfile {

    override suspend fun getAddressList(search: String?, types: String, sort: Sort): List<Address> {
        val user = getUser()
        val query = query {
            select("SELECT * FROM address")
            whereGroup {
                where("user_id = :user_id")
                parameter("user_id", user.id)
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
        return databaseClient.execute(query.sql).bindMap(query.parameters).`as`(Address::class.java).fetch().flow()
            .toList()
    }

    override suspend fun addAddress(address: Address): Address {
        val user = getUser()
        val newAddress = address.copy(id = null, userId = user.id, createdAt = OffsetDateTime.now())
        val id = databaseClient.insert().into(Address::class.java).using(newAddress)
            .map { row -> row.get("id", java.lang.Integer::class.java) }.awaitOne()
        return newAddress.copy(id = id?.toInt())
    }

    override suspend fun updateAddress(address: Address): Address {
        val user = getUser()
        address.id?.let { id ->
            databaseClient.select().from(Address::class.java).matching(
                where("id").`is`(id).and("user_id").`is`(
                    user.id ?: 0
                )
            ).fetch().awaitOneOrNull()?.let { oldAddress ->
                val newAddress = address.copy(userId = user.id, createdAt = oldAddress.createdAt)
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

@Prototype
actual class ProfileService(override val httpRequest: HttpRequest<*>, override val databaseClient: DatabaseClient) :
    IProfileService, WithProfile {
    override suspend fun getProfile(): Profile {
        val user = getUser()
        return Profile(user.name, user.username)
    }
}

@Prototype
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
