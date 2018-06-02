package com.example

import kotlinx.coroutines.experimental.Deferred
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.Request
import pl.treksoft.kvision.remote.ServiceManager

enum class Sort {
    FN, LN, E, F
}

expect class AddressService() {
    fun getAddressList(search: String?, types: String, sort: Sort, req: Request? = null): Deferred<List<Address>>
    fun addAddress(address: Address, req: Request? = null): Deferred<Address>
    fun updateAddress(address: Address, req: Request? = null): Deferred<Address>
    fun deleteAddress(id: Int, req: Request? = null): Deferred<Boolean>
}

expect class ProfileService() {
    fun getProfile(req: Request? = null): Deferred<Profile>
}

expect class RegisterProfileService() {
    fun registerProfile(profile: Profile, password: String, req: Request? = null): Deferred<Boolean>
}

object AddressServiceManager : ServiceManager<AddressService>(AddressService()) {
    init {
        bind(AddressService::getAddressList)
        bind(AddressService::addAddress)
        bind(AddressService::updateAddress)
        bind(AddressService::deleteAddress)
    }
}

object ProfileServiceManager : ServiceManager<ProfileService>(ProfileService()) {
    init {
        bind(ProfileService::getProfile)
    }
}

object RegisterProfileServiceManager : ServiceManager<RegisterProfileService>(RegisterProfileService()) {
    init {
        bind(RegisterProfileService::registerProfile)
    }
}
