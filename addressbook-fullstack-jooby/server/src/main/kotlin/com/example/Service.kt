package com.example

import com.github.andrewoma.kwery.core.ThreadLocalSession
import com.github.andrewoma.kwery.core.interceptor.LoggingInterceptor
import com.typesafe.config.Config
import org.jooby.require
import org.pac4j.sql.profile.DbProfile
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.Request
import pl.treksoft.kvision.remote.async
import pl.treksoft.kvision.remote.asyncAuth
import java.util.*
import javax.sql.DataSource

actual class AddressService : IAddressService {

    private fun getAddressDao(req: Request): AddressDao {
        val db = req.require("db", DataSource::class)
        val session = ThreadLocalSession(db, getDbDialect(req.require(Config::class)), LoggingInterceptor())
        return AddressDao(session)
    }

    override fun getAddressList(search: String?, types: String, sort: Sort, req: Request?) =
        req.asyncAuth { request, _, profile ->
            getAddressDao(request).findByCriteria(profile.id, search, types, sort)
        }

    override fun addAddress(address: Address, req: Request?) = req.asyncAuth { request, _, profile ->
        getAddressDao(request).insert(address.copy(userId = profile.id, createdAt = Date()))
    }

    override fun updateAddress(address: Address, req: Request?) = req.asyncAuth { request, _, profile ->
        val dao = getAddressDao(request)
        address.id?.let {
            val oldAddress = dao.findByIdForUpdate(it)
            dao.unsafeUpdate(address.copy(userId = profile.id, createdAt = oldAddress?.createdAt))
        } ?: throw IllegalArgumentException("The ID of the address not set")
    }

    override fun deleteAddress(id: Int, req: Request?) = req.async { request ->
        getAddressDao(request).delete(id) > 0
    }

}

actual class ProfileService : IProfileService {

    override fun getProfile(req: Request?) = req.asyncAuth { _, _, profile ->
        profile
    }

}

actual class RegisterProfileService : IRegisterProfileService {

    override fun registerProfile(profile: Profile, password: String, req: Request?) = req.async { request ->
        val profileService = request.require(MyDbProfileService::class)
        val dbProfile = DbProfile()
        dbProfile.build(profile.id, profile.attributes)
        profileService.create(dbProfile, password)
        true
    }

}
