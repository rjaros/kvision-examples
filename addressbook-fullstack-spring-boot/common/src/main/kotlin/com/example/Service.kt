package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.SpringServiceManager

enum class Sort {
    FN, LN, E, F
}

expect class AddressService() {
    fun getAddressList(search: String?, types: String, sort: Sort): Deferred<List<Address>>
    fun addAddress(address: Address): Deferred<Address>
    fun updateAddress(address: Address): Deferred<Address>
    fun deleteAddress(id: Int): Deferred<Boolean>
}

expect class ProfileService() {
    fun getProfile(): Deferred<Profile>
}

expect class RegisterProfileService() {
    fun registerProfile(profile: Profile, password: String): Deferred<Boolean>
}

object AddressServiceManager : SpringServiceManager<AddressService>(AddressService::class) {
    init {
        bind(AddressService::getAddressList)
        bind(AddressService::addAddress)
        bind(AddressService::updateAddress)
        bind(AddressService::deleteAddress)
    }
}

object ProfileServiceManager : SpringServiceManager<ProfileService>(ProfileService::class) {
    init {
        bind(ProfileService::getProfile)
    }
}

object RegisterProfileServiceManager : SpringServiceManager<RegisterProfileService>(RegisterProfileService::class) {
    init {
        bind(RegisterProfileService::registerProfile)
    }
}
