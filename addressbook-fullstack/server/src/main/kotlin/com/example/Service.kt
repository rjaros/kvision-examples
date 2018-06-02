package com.example

import com.github.andrewoma.kwery.core.ThreadLocalSession
import com.github.andrewoma.kwery.core.dialect.HsqlDialect
import com.github.andrewoma.kwery.core.interceptor.LoggingInterceptor
import kotlinx.coroutines.experimental.Deferred
import org.jooby.require
import org.pac4j.sql.profile.DbProfile
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.Request
import pl.treksoft.kvision.remote.async
import pl.treksoft.kvision.types.KDate
import javax.sql.DataSource

actual class AddressService actual constructor() {

    private fun getAddressDao(req: Request): AddressDao {
        val db = req.require("db", DataSource::class)
        val session = ThreadLocalSession(db, HsqlDialect(), LoggingInterceptor())
        return AddressDao(session)
    }

    actual fun getAddressList(search: String?, types: String, sort: Sort, req: Request?): Deferred<List<Address>> =
        req.async { request, _, profile ->
            getAddressDao(request).findByCriteria(profile.id, search, types, sort)
        }

    actual fun addAddress(address: Address, req: Request?): Deferred<Address> = req.async { request, _, profile ->
        getAddressDao(request).insert(address.copy(userId = profile.id, createdAt = KDate()))
    }

    actual fun updateAddress(address: Address, req: Request?): Deferred<Address> = req.async { request, _, profile ->
        val dao = getAddressDao(request)
        address.id?.let {
            val oldAddress = dao.findByIdForUpdate(it)
            dao.unsafeUpdate(address.copy(userId = profile.id, createdAt = oldAddress?.createdAt))
        } ?: throw IllegalArgumentException("The ID of the address not set")
    }

    actual fun deleteAddress(id: Int, req: Request?): Deferred<Boolean> = req.async { request ->
        getAddressDao(request).delete(id) > 0
    }
}

actual class ProfileService actual constructor() {

    actual fun getProfile(req: Request?): Deferred<Profile> {
        return req.async { _, _, profile ->
            profile
        }
    }
}

actual class RegisterProfileService actual constructor() {

    actual fun registerProfile(profile: Profile, password: String, req: Request?): Deferred<Boolean> {
        return req.async { request ->
            val profileService = request.require(MyDbProfileService::class)
            val dbProfile = DbProfile()
            dbProfile.build(profile.id, profile.attributes)
            profileService.create(dbProfile, password)
            true
        }
    }
}
