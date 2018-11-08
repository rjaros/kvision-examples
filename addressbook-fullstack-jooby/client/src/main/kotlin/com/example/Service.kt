package com.example

import pl.treksoft.kvision.remote.JoobyRemoteAgent
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.Request

object AddressAgent : JoobyRemoteAgent<AddressService>(AddressServiceManager)

object ProfileAgent : JoobyRemoteAgent<ProfileService>(ProfileServiceManager)

object RegisterProfileAgent : JoobyRemoteAgent<RegisterProfileService>(RegisterProfileServiceManager)

actual class AddressService : IAddressService {
    override fun getAddressList(search: String?, types: String, sort: Sort, req: Request?) =
        AddressAgent.call(IAddressService::getAddressList, search, types, sort)

    override fun addAddress(address: Address, req: Request?) = AddressAgent.call(IAddressService::addAddress, address)

    override fun updateAddress(address: Address, req: Request?) =
        AddressAgent.call(IAddressService::updateAddress, address)

    override fun deleteAddress(id: Int, req: Request?) = AddressAgent.call(IAddressService::deleteAddress, id)
}

actual class ProfileService : IProfileService {
    override fun getProfile(req: Request?) = ProfileAgent.call(IProfileService::getProfile)
}

actual class RegisterProfileService : IRegisterProfileService {
    override fun registerProfile(profile: Profile, password: String, req: Request?) =
        RegisterProfileAgent.call(IRegisterProfileService::registerProfile, profile, password)
}
