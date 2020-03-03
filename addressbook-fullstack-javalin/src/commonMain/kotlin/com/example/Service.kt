package com.example

import pl.treksoft.kvision.annotations.KVService

enum class Sort {
    FN, LN, E, F
}

@KVService
interface IAddressService {
    suspend fun getAddressList(search: String?, types: String, sort: Sort): List<Address>
    suspend fun addAddress(address: Address): Address
    suspend fun updateAddress(address: Address): Address
    suspend fun deleteAddress(id: Int): Boolean
}

@KVService
interface IProfileService {
    suspend fun getProfile(): Profile
}

@KVService
interface IRegisterProfileService {
    suspend fun registerProfile(profile: Profile, password: String): Boolean
}
