package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.SpringRemoteAgent

object AddressAgent : SpringRemoteAgent<AddressService>(AddressServiceManager)

object ProfileAgent : SpringRemoteAgent<ProfileService>(ProfileServiceManager)

object RegisterProfileAgent : SpringRemoteAgent<RegisterProfileService>(RegisterProfileServiceManager)

actual class AddressService actual constructor() {
    actual fun getAddressList(search: String?, types: String, sort: Sort): Deferred<List<Address>> =
        AddressAgent.call(AddressService::getAddressList, search, types, sort)

    actual fun addAddress(address: Address): Deferred<Address> =
        AddressAgent.call(AddressService::addAddress, address)

    actual fun updateAddress(address: Address): Deferred<Address> =
        AddressAgent.call(AddressService::updateAddress, address)

    actual fun deleteAddress(id: Int): Deferred<Boolean> =
        AddressAgent.call(AddressService::deleteAddress, id)
}

actual class ProfileService actual constructor() {
    actual fun getProfile(): Deferred<Profile> =
        ProfileAgent.call(ProfileService::getProfile)
}

actual class RegisterProfileService actual constructor() {
    actual fun registerProfile(profile: Profile, password: String): Deferred<Boolean> =
        RegisterProfileAgent.call(RegisterProfileService::registerProfile, profile, password)
}
