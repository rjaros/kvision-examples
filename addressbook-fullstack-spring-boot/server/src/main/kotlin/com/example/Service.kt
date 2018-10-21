package com.example

import kotlinx.coroutines.Deferred
import org.pac4j.core.credentials.password.SpringSecurityPasswordEncoder
import org.pac4j.sql.profile.DbProfile
import org.pac4j.sql.profile.service.DbProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.async
import pl.treksoft.kvision.remote.asyncAuth
import java.util.*
import javax.sql.DataSource

@Service
actual class AddressService actual constructor() {

    @Autowired
    lateinit var addressDao: AddressDao

    actual fun getAddressList(search: String?, types: String, sort: Sort): Deferred<List<Address>> =
        asyncAuth { profile ->
            addressDao.findByCriteria(profile.id, search, types, sort)
        }

    actual fun addAddress(address: Address): Deferred<Address> = asyncAuth { profile ->
        addressDao.insert(address.copy(userId = profile.id, createdAt = Date()))
    }

    actual fun updateAddress(address: Address): Deferred<Address> = asyncAuth { profile ->
        address.id?.let {
            val oldAddress = addressDao.findByIdForUpdate(it)
            addressDao.unsafeUpdate(address.copy(userId = profile.id, createdAt = oldAddress?.createdAt))
        } ?: throw IllegalArgumentException("The ID of the address not set")
    }

    actual fun deleteAddress(id: Int): Deferred<Boolean> = asyncAuth {
        addressDao.delete(id) > 0
    }
}

@Service
actual class ProfileService actual constructor() {
    actual fun getProfile(): Deferred<Profile> = asyncAuth { it }
}

@Service
class MyDbProfileService constructor(ds: DataSource) :
    DbProfileService(ds, SpringSecurityPasswordEncoder(BCryptPasswordEncoder()))

@Service
actual class RegisterProfileService actual constructor() {

    @Autowired
    lateinit var profileService: MyDbProfileService

    actual fun registerProfile(profile: Profile, password: String): Deferred<Boolean> = async {
        val dbProfile = DbProfile()
        dbProfile.build(profile.id, profile.attributes)
        profileService.create(dbProfile, password)
        true
    }
}
