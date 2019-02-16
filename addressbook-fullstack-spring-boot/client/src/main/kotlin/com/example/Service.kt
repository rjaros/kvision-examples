package com.example

import pl.treksoft.kvision.remote.KVRemoteAgent
import pl.treksoft.kvision.remote.Profile

actual class AddressService : IAddressService, KVRemoteAgent<AddressService>(AddressServiceManager) {
    override suspend fun getAddressList(search: String?, types: String, sort: Sort) =
        call(IAddressService::getAddressList, search, types, sort)

    override suspend fun addAddress(address: Address) = call(IAddressService::addAddress, address)

    override suspend fun updateAddress(address: Address) = call(IAddressService::updateAddress, address)

    override suspend fun deleteAddress(id: Int) = call(IAddressService::deleteAddress, id)
}

actual class ProfileService : IProfileService, KVRemoteAgent<ProfileService>(ProfileServiceManager) {
    override suspend fun getProfile() = call(IProfileService::getProfile)
}

actual class RegisterProfileService : IRegisterProfileService,
    KVRemoteAgent<RegisterProfileService>(RegisterProfileServiceManager) {
    override suspend fun registerProfile(profile: Profile, password: String) =
        call(IRegisterProfileService::registerProfile, profile, password)
}
