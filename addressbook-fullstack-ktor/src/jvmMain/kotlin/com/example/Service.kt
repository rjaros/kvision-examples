package com.example

import com.example.Db.dbQuery
import com.example.Db.queryList
import com.github.andrewoma.kwery.core.builder.query
import io.ktor.server.application.ApplicationCall
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import java.sql.ResultSet
import java.time.ZoneId

suspend fun <RESP> ApplicationCall.withProfile(block: suspend (Profile) -> RESP): RESP {
    val profile = this.sessions.get<Profile>()
    return profile?.let {
        block(profile)
    } ?: throw IllegalStateException("Profile not set!")
}

class AddressService(private val call: ApplicationCall) : IAddressService {

    override suspend fun getAddressList(search: String?, types: String, sort: Sort) =
        call.withProfile { profile ->
            dbQuery {
                val query = query {
                    select("SELECT * FROM address")
                    whereGroup {
                        where("user_id = :user_id")
                        parameter("user_id", profile.id)
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
                queryList(query.sql, query.parameters) {
                    toAddress(it)
                }
            }
        }

    override suspend fun addAddress(address: Address) = call.withProfile { profile ->
        val key = dbQuery {
            (AddressDao.insert {
                it[firstName] = address.firstName
                it[lastName] = address.lastName
                it[email] = address.email
                it[phone] = address.phone
                it[postalAddress] = address.postalAddress
                it[favourite] = address.favourite ?: false
                it[createdAt] = DateTime()
                it[userId] = profile.id!!

            } get AddressDao.id)
        }
        getAddress(key)!!
    }

    override suspend fun updateAddress(address: Address) = call.withProfile { profile ->
        address.id?.let {
            getAddress(it)?.let { oldAddress ->
                dbQuery {
                    AddressDao.update({ AddressDao.id eq it }) {
                        it[firstName] = address.firstName
                        it[lastName] = address.lastName
                        it[email] = address.email
                        it[phone] = address.phone
                        it[postalAddress] = address.postalAddress
                        it[favourite] = address.favourite ?: false
                        it[createdAt] = oldAddress.createdAt
                            ?.let { DateTime(java.util.Date.from(it.atZone(ZoneId.systemDefault()).toInstant())) }
                        it[userId] = profile.id!!
                    }
                }
            }
            getAddress(it)
        } ?: throw IllegalArgumentException("The ID of the address not set")
    }

    override suspend fun deleteAddress(id: Int): Boolean = call.withProfile { profile ->
        dbQuery {
            AddressDao.deleteWhere { (AddressDao.userId eq profile.id!!) and (AddressDao.id eq id) } > 0
        }
    }

    private suspend fun getAddress(id: Int): Address? = dbQuery {
        AddressDao.select {
            AddressDao.id eq id
        }.mapNotNull { toAddress(it) }.singleOrNull()
    }

    private fun toAddress(row: ResultRow): Address =
        Address(
            id = row[AddressDao.id],
            firstName = row[AddressDao.firstName],
            lastName = row[AddressDao.lastName],
            email = row[AddressDao.email],
            phone = row[AddressDao.phone],
            postalAddress = row[AddressDao.postalAddress],
            favourite = row[AddressDao.favourite],
            createdAt = row[AddressDao.createdAt]?.millis?.let { java.util.Date(it) }?.toInstant()
                ?.atZone(ZoneId.systemDefault())?.toLocalDateTime(),
            userId = row[AddressDao.userId]
        )

    private fun toAddress(rs: ResultSet): Address =
        Address(
            id = rs.getInt(AddressDao.id.name),
            firstName = rs.getString(AddressDao.firstName.name),
            lastName = rs.getString(AddressDao.lastName.name),
            email = rs.getString(AddressDao.email.name),
            phone = rs.getString(AddressDao.phone.name),
            postalAddress = rs.getString(AddressDao.postalAddress.name),
            favourite = rs.getBoolean(AddressDao.favourite.name),
            createdAt = rs.getTimestamp(AddressDao.createdAt.name)?.toInstant()
                ?.atZone(ZoneId.systemDefault())?.toLocalDateTime(),
            userId = rs.getInt(AddressDao.userId.name)
        )
}

class ProfileService(private val call: ApplicationCall) : IProfileService {

    override suspend fun getProfile() = call.withProfile { it }

}

class RegisterProfileService : IRegisterProfileService {

    override suspend fun registerProfile(profile: Profile, password: String): Boolean {
        try {
            dbQuery {
                UserDao.insert {
                    it[this.name] = profile.name!!
                    it[this.username] = profile.username!!
                    it[this.password] = DigestUtils.sha256Hex(password)
                }
            }
        } catch (e: Exception) {
            throw Exception("Register operation failed!")
        }
        return true
    }

}
