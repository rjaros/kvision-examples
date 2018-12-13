package com.example

import org.pac4j.core.credentials.password.SpringSecurityPasswordEncoder
import org.pac4j.sql.profile.DbProfile
import org.pac4j.sql.profile.service.DbProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.withProfile
import java.util.*
import javax.sql.DataSource

@Service
actual class AddressService : IAddressService {

    @Autowired
    lateinit var addressDao: AddressDao

    override suspend fun getAddressList(search: String?, types: String, sort: Sort) = withProfile { profile ->
        addressDao.findByCriteria(profile.id, search, types, sort)
    }

    override suspend fun addAddress(address: Address) = withProfile { profile ->
        addressDao.insert(address.copy(userId = profile.id, createdAt = Date()))
    }

    override suspend fun updateAddress(address: Address) = withProfile { profile ->
        address.id?.let {
            val oldAddress = addressDao.findByIdForUpdate(it)
            addressDao.unsafeUpdate(address.copy(userId = profile.id, createdAt = oldAddress?.createdAt))
        } ?: throw IllegalArgumentException("The ID of the address not set")
    }

    override suspend fun deleteAddress(id: Int) = withProfile {
        addressDao.delete(id) > 0
    }
}

@Service
actual class ProfileService : IProfileService {
    override suspend fun getProfile() = withProfile { it }
}

@Service
class MyDbProfileService constructor(ds: DataSource) :
    DbProfileService(ds, SpringSecurityPasswordEncoder(BCryptPasswordEncoder()))

@Service
actual class RegisterProfileService : IRegisterProfileService {

    @Autowired
    lateinit var profileService: MyDbProfileService

    override suspend fun registerProfile(profile: Profile, password: String): Boolean {
        val dbProfile = DbProfile()
        dbProfile.build(profile.id, profile.attributes)
        profileService.create(dbProfile, password)
        return true
    }
}
