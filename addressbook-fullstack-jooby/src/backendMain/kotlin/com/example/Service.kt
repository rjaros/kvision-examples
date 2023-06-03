package com.example

import com.github.andrewoma.kwery.core.ThreadLocalSession
import com.github.andrewoma.kwery.core.interceptor.LoggingInterceptor
import com.google.inject.Inject
import com.typesafe.config.Config
import io.jooby.Context
import org.pac4j.sql.profile.DbProfile
import java.time.LocalDateTime
import jakarta.inject.Named
import javax.sql.DataSource

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class AddressService @Inject constructor(override val ctx: Context) : IAddressService, WithContext {

    @Inject
    lateinit var config: Config

    @Inject
    @Named("db")
    lateinit var dataSource: DataSource

    private fun getAddressDao(): AddressDao {
        val session = ThreadLocalSession(dataSource, getDbDialect(config), LoggingInterceptor())
        return AddressDao(session)
    }

    override suspend fun getAddressList(search: String?, types: String, sort: Sort) = withProfile { profile ->
        getAddressDao().findByCriteria(profile.id!!, search, types, sort)
    }

    override suspend fun addAddress(address: Address) = withProfile { profile ->
        getAddressDao().insert(address.copy(userId = profile.id, createdAt = LocalDateTime.now()))
    }

    override suspend fun updateAddress(address: Address) = withProfile { profile ->
        val dao = getAddressDao()
        address.id?.let {
            val oldAddress = dao.findByIdForUpdate(it)
            dao.unsafeUpdate(address.copy(userId = profile.id, createdAt = oldAddress?.createdAt))
        } ?: throw IllegalArgumentException("The ID of the address not set")
    }

    override suspend fun deleteAddress(id: Int) = withProfile {
        getAddressDao().delete(id) > 0
    }

}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class ProfileService @Inject constructor(override val ctx: Context) : IProfileService, WithContext {

    override suspend fun getProfile() = withProfile { it }

}

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class RegisterProfileService : IRegisterProfileService {

    @Inject
    lateinit var profileService: MyDbProfileService

    override suspend fun registerProfile(profile: Profile, password: String): Boolean {
        val dbProfile = DbProfile()
        dbProfile.build(profile.id, profile.attributes as MutableMap<String, Any>)
        profileService.create(dbProfile, password)
        return true
    }

}
