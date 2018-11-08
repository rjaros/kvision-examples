package com.example

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
actual class AddressService : IAddressService {

    @Autowired
    lateinit var addressDao: AddressDao

    override fun getAddressList(search: String?, types: String, sort: Sort) = asyncAuth { profile ->
        addressDao.findByCriteria(profile.id, search, types, sort)
    }

    override fun addAddress(address: Address) = asyncAuth { profile ->
        addressDao.insert(address.copy(userId = profile.id, createdAt = Date()))
    }

    override fun updateAddress(address: Address) = asyncAuth { profile ->
        address.id?.let {
            val oldAddress = addressDao.findByIdForUpdate(it)
            addressDao.unsafeUpdate(address.copy(userId = profile.id, createdAt = oldAddress?.createdAt))
        } ?: throw IllegalArgumentException("The ID of the address not set")
    }

    override fun deleteAddress(id: Int) = asyncAuth {
        addressDao.delete(id) > 0
    }
}

@Service
actual class ProfileService : IProfileService {
    override fun getProfile() = asyncAuth { it }
}

@Service
class MyDbProfileService constructor(ds: DataSource) :
    DbProfileService(ds, SpringSecurityPasswordEncoder(BCryptPasswordEncoder()))

@Service
actual class RegisterProfileService : IRegisterProfileService {

    @Autowired
    lateinit var profileService: MyDbProfileService

    override fun registerProfile(profile: Profile, password: String) = async {
        val dbProfile = DbProfile()
        dbProfile.build(profile.id, profile.attributes)
        profileService.create(dbProfile, password)
        true
    }
}
