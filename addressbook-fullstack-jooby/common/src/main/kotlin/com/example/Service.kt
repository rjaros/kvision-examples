package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.JoobyServiceManager
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.Request

enum class Sort {
    FN, LN, E, F
}

interface IAddressService {
    fun getAddressList(search: String?, types: String, sort: Sort, req: Request? = null): Deferred<List<Address>>
    fun addAddress(address: Address, req: Request? = null): Deferred<Address>
    fun updateAddress(address: Address, req: Request? = null): Deferred<Address>
    fun deleteAddress(id: Int, req: Request? = null): Deferred<Boolean>
}

interface IProfileService {
    fun getProfile(req: Request? = null): Deferred<Profile>
}

interface IRegisterProfileService {
    fun registerProfile(profile: Profile, password: String, req: Request? = null): Deferred<Boolean>
}

expect class AddressService() : IAddressService
expect class ProfileService() : IProfileService
expect class RegisterProfileService() : IRegisterProfileService

object AddressServiceManager : JoobyServiceManager<AddressService>(AddressService()) {
    init {
        bind(AddressService::getAddressList)
        bind(AddressService::addAddress)
        bind(AddressService::updateAddress)
        bind(AddressService::deleteAddress)
    }
}

object ProfileServiceManager : JoobyServiceManager<ProfileService>(ProfileService()) {
    init {
        bind(ProfileService::getProfile)
    }
}

object RegisterProfileServiceManager : JoobyServiceManager<RegisterProfileService>(RegisterProfileService()) {
    init {
        bind(RegisterProfileService::registerProfile)
    }
}
