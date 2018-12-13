package com.example

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.remote.JoobyServiceManager
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.Request

enum class Sort {
    FN, LN, E, F
}

interface IAddressService {
    suspend fun getAddressList(search: String?, types: String, sort: Sort, req: Request? = null): List<Address>
    suspend fun addAddress(address: Address, req: Request? = null): Address
    suspend fun updateAddress(address: Address, req: Request? = null): Address
    suspend fun deleteAddress(id: Int, req: Request? = null): Boolean
}

interface IProfileService {
    suspend fun getProfile(req: Request? = null): Profile
}

interface IRegisterProfileService {
    suspend fun registerProfile(profile: Profile, password: String, req: Request? = null): Boolean
}

expect class AddressService() : IAddressService
expect class ProfileService() : IProfileService
expect class RegisterProfileService() : IRegisterProfileService

object AddressServiceManager : JoobyServiceManager<AddressService>(AddressService()) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(IAddressService::getAddressList)
            bind(IAddressService::addAddress)
            bind(IAddressService::updateAddress)
            bind(IAddressService::deleteAddress)
        }
    }
}

object ProfileServiceManager : JoobyServiceManager<ProfileService>(ProfileService()) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(IProfileService::getProfile)
        }
    }
}

object RegisterProfileServiceManager : JoobyServiceManager<RegisterProfileService>(RegisterProfileService()) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(IRegisterProfileService::registerProfile)
        }
    }
}
