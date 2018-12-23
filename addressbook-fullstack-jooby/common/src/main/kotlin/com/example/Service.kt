package com.example

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.remote.KVServiceManager
import pl.treksoft.kvision.remote.Profile

enum class Sort {
    FN, LN, E, F
}

interface IAddressService {
    suspend fun getAddressList(search: String?, types: String, sort: Sort): List<Address>
    suspend fun addAddress(address: Address): Address
    suspend fun updateAddress(address: Address): Address
    suspend fun deleteAddress(id: Int): Boolean
}

interface IProfileService {
    suspend fun getProfile(): Profile
}

interface IRegisterProfileService {
    suspend fun registerProfile(profile: Profile, password: String): Boolean
}

expect class AddressService : IAddressService
expect class ProfileService : IProfileService
expect class RegisterProfileService : IRegisterProfileService

object AddressServiceManager : KVServiceManager<AddressService>(AddressService::class) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(IAddressService::getAddressList)
            bind(IAddressService::addAddress)
            bind(IAddressService::updateAddress)
            bind(IAddressService::deleteAddress)
        }
    }
}

object ProfileServiceManager : KVServiceManager<ProfileService>(ProfileService::class) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(IProfileService::getProfile)
        }
    }
}

object RegisterProfileServiceManager : KVServiceManager<RegisterProfileService>(RegisterProfileService::class) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(IRegisterProfileService::registerProfile)
        }
    }
}
