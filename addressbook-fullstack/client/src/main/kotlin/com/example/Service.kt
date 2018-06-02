package com.example

import kotlinx.coroutines.experimental.Deferred
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.RemoteAgent
import pl.treksoft.kvision.remote.Request

object AddressAgent : RemoteAgent<AddressService>(AddressServiceManager)

object ProfileAgent : RemoteAgent<ProfileService>(ProfileServiceManager)

object RegisterProfileAgent : RemoteAgent<RegisterProfileService>(RegisterProfileServiceManager)

actual class AddressService actual constructor() {
    actual fun getAddressList(search: String?, types: String, sort: Sort, req: Request?): Deferred<List<Address>> =
        AddressAgent.call(AddressService::getAddressList, search, types, sort)

    actual fun addAddress(address: Address, req: Request?): Deferred<Address> =
        AddressAgent.call(AddressService::addAddress, address)

    actual fun updateAddress(address: Address, req: Request?): Deferred<Address> =
        AddressAgent.call(AddressService::updateAddress, address)

    actual fun deleteAddress(id: Int, req: Request?): Deferred<Boolean> =
        AddressAgent.call(AddressService::deleteAddress, id)
}

actual class ProfileService actual constructor() {
    actual fun getProfile(req: Request?): Deferred<Profile> =
        ProfileAgent.call(ProfileService::getProfile)
}

actual class RegisterProfileService actual constructor() {
    actual fun registerProfile(profile: Profile, password: String, req: Request?): Deferred<Boolean> =
        RegisterProfileAgent.call(RegisterProfileService::registerProfile, profile, password)
}
