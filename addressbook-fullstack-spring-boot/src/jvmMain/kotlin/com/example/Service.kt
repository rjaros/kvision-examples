package com.example

import io.kvision.types.OffsetDateTime
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.r2dbc.core.awaitOne
import org.springframework.r2dbc.core.awaitOneOrNull
import org.springframework.r2dbc.core.awaitRowsUpdated
import org.springframework.r2dbc.core.flow
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import pl.treksoft.e4k.core.DbClient
import pl.treksoft.e4k.core.delete
import pl.treksoft.e4k.core.execute
import pl.treksoft.e4k.core.insert
import pl.treksoft.e4k.core.setNullable
import pl.treksoft.e4k.core.update
import pl.treksoft.e4k.core.valueNullable
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
class AddressService(override val serverRequest: ServerRequest, private val dbClient: DbClient) :
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
        val id = dbClient.insert().into("address", "id")
            .valueNullable("first_name", address.firstName)
            .valueNullable("last_name", address.lastName)
            .valueNullable("email", address.email)
            .valueNullable("phone", address.phone)
            .valueNullable("postal_address", address.postalAddress)
            .value("favourite", address.favourite == true)
            .value("created_at", OffsetDateTime.now())
            .value("user_id", profile.id!!.toInt())
            .awaitOne()
        return dbClient.execute<Address>("SELECT * FROM address WHERE id = :id")
            .bind("id", id).fetch().awaitOne()
    }

    override suspend fun updateAddress(address: Address): Address {
        val profile = getProfile()
        val id = address.id ?: throw IllegalArgumentException("The ID of the address is not set")
        dbClient.execute<Address>("SELECT * FROM address WHERE id = :id AND user_id = :userId")
            .bind("id", id).bind("userId", profile.id!!.toInt())
            .fetch().awaitOneOrNull() ?: throw IllegalArgumentException("Address not found")
        dbClient.update().table("address").using {
            Update.setNullable("first_name", address.firstName)
                .setNullable("last_name", address.lastName)
                .setNullable("email", address.email)
                .setNullable("phone", address.phone)
                .setNullable("postal_address", address.postalAddress)
                .set("favourite", address.favourite == true)
        }.matching("id = :id", mapOf("id" to id)).fetch().awaitRowsUpdated()
        return dbClient.execute<Address>("SELECT * FROM address WHERE id = :id")
            .bind("id", id).fetch().awaitOne()
    }

    override suspend fun deleteAddress(id: Int): Boolean {
        return dbClient.delete().from("address")
            .matching("id = :id", mapOf("id" to id)).fetch().awaitRowsUpdated() == 1L
    }
}

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ProfileService(override val serverRequest: ServerRequest) : IProfileService, WithProfile {
    override suspend fun getProfile(): Profile {
        return super.getProfile()
    }
}

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class RegisterProfileService(
    private val dbClient: DbClient,
    private val passwordEncoder: PasswordEncoder
) : IRegisterProfileService {

    override suspend fun registerProfile(profile: Profile, password: String): Boolean {
        try {
            dbClient.insert().into(User::class.java).using(
                User(
                    username = profile.username,
                    name = profile.name!!,
                    password = passwordEncoder.encode(password)!!
                )
            ).awaitSingle()
        } catch (e: Exception) {
            throw Exception("Register operation failed!")
        }
        return true
    }
}
