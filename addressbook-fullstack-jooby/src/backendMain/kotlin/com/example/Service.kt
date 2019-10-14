package com.example

import com.github.andrewoma.kwery.core.ThreadLocalSession
import com.github.andrewoma.kwery.core.interceptor.LoggingInterceptor
import com.google.inject.Inject
import com.typesafe.config.Config
import org.jooby.Request
import org.pac4j.sql.profile.DbProfile
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.withProfile
import java.time.LocalDateTime
import java.util.*
import javax.inject.Named
import javax.sql.DataSource

actual class AddressService : IAddressService {

    @Inject
    lateinit var request: Request

    @Inject
    lateinit var config: Config

    @Inject
    @Named("db")
    lateinit var dataSource: DataSource

    private fun getAddressDao(): AddressDao {
        val session = ThreadLocalSession(dataSource, getDbDialect(config), LoggingInterceptor())
        return AddressDao(session)
    }

    override suspend fun getAddressList(search: String?, types: String, sort: Sort) =
        request.withProfile { profile ->
            getAddressDao().findByCriteria(profile.id, search, types, sort)
        }

    override suspend fun addAddress(address: Address) = request.withProfile { profile ->
        getAddressDao().insert(address.copy(userId = profile.id, createdAt = LocalDateTime.now()))
    }

    override suspend fun updateAddress(address: Address) = request.withProfile { profile ->
        val dao = getAddressDao()
        address.id?.let {
            val oldAddress = dao.findByIdForUpdate(it)
            dao.unsafeUpdate(address.copy(userId = profile.id, createdAt = oldAddress?.createdAt))
        } ?: throw IllegalArgumentException("The ID of the address not set")
    }

    override suspend fun deleteAddress(id: Int) = request.withProfile {
        getAddressDao().delete(id) > 0
    }

}

actual class ProfileService : IProfileService {

    @Inject
    lateinit var request: Request

    override suspend fun getProfile() = request.withProfile { it }

}

actual class RegisterProfileService : IRegisterProfileService {

    @Inject
    lateinit var profileService: MyDbProfileService

    override suspend fun registerProfile(profile: Profile, password: String): Boolean {
        val dbProfile = DbProfile()
        dbProfile.build(profile.id, profile.attributes)
        profileService.create(dbProfile, password)
        return true
    }

}
