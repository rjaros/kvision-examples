package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.SpringServiceManager

enum class Sort {
    FN, LN, E, F
}

interface IAddressService {
    fun getAddressList(search: String?, types: String, sort: Sort): Deferred<List<Address>>
    fun addAddress(address: Address): Deferred<Address>
    fun updateAddress(address: Address): Deferred<Address>
    fun deleteAddress(id: Int): Deferred<Boolean>
}

interface IProfileService {
    fun getProfile(): Deferred<Profile>
}

interface IRegisterProfileService {
    fun registerProfile(profile: Profile, password: String): Deferred<Boolean>
}

expect class AddressService : IAddressService
expect class ProfileService : IProfileService
expect class RegisterProfileService : IRegisterProfileService

object AddressServiceManager : SpringServiceManager<AddressService>(AddressService::class) {
    init {
        bind(IAddressService::getAddressList)
        bind(IAddressService::addAddress)
        bind(IAddressService::updateAddress)
        bind(IAddressService::deleteAddress)
    }
}

object ProfileServiceManager : SpringServiceManager<ProfileService>(ProfileService::class) {
    init {
        bind(IProfileService::getProfile)
    }
}

object RegisterProfileServiceManager : SpringServiceManager<RegisterProfileService>(RegisterProfileService::class) {
    init {
        bind(IRegisterProfileService::registerProfile)
    }
}
