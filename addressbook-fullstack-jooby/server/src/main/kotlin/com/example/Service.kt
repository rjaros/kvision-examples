package com.example

import com.github.andrewoma.kwery.core.ThreadLocalSession
import com.github.andrewoma.kwery.core.interceptor.LoggingInterceptor
import com.google.inject.Inject
import com.google.inject.Singleton
import com.typesafe.config.Config
import org.jooby.require
import org.pac4j.sql.profile.DbProfile
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.Request
import pl.treksoft.kvision.remote.withProfile
import java.util.*
import javax.sql.DataSource

@Singleton
actual class AddressService : IAddressService {

    @Inject
    lateinit var request: Request

    private fun getAddressDao(req: Request): AddressDao {
        val db = req.require("db", DataSource::class)
        val session = ThreadLocalSession(db, getDbDialect(req.require(Config::class)), LoggingInterceptor())
        return AddressDao(session)
    }

    override suspend fun getAddressList(search: String?, types: String, sort: Sort) =
        request.withProfile { profile ->
            getAddressDao(request).findByCriteria(profile.id, search, types, sort)
        }

    override suspend fun addAddress(address: Address) = request.withProfile { profile ->
        getAddressDao(request).insert(address.copy(userId = profile.id, createdAt = Date()))
    }

    override suspend fun updateAddress(address: Address) = request.withProfile { profile ->
        val dao = getAddressDao(request)
        address.id?.let {
            val oldAddress = dao.findByIdForUpdate(it)
            dao.unsafeUpdate(address.copy(userId = profile.id, createdAt = oldAddress?.createdAt))
        } ?: throw IllegalArgumentException("The ID of the address not set")
    }

    override suspend fun deleteAddress(id: Int): Boolean {
        return getAddressDao(request).delete(id) > 0
    }

}

@Singleton
actual class ProfileService : IProfileService {

    @Inject
    lateinit var request: Request

    override suspend fun getProfile() = request.withProfile { it }

}

@Singleton
actual class RegisterProfileService : IRegisterProfileService {

    @Inject
    lateinit var request: Request

    @Inject
    lateinit var profileService: MyDbProfileService

    override suspend fun registerProfile(profile: Profile, password: String): Boolean {
        val dbProfile = DbProfile()
        dbProfile.build(profile.id, profile.attributes)
        profileService.create(dbProfile, password)
        return true
    }

}
