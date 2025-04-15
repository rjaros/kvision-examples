package com.example

import dev.kilua.rpc.annotations.RpcService
import kotlinx.serialization.Serializable

@Serializable
enum class Sort {
    FN, LN, E, F
}

@RpcService
interface IAddressService {
    suspend fun getAddressList(search: String?, types: String, sort: Sort): List<Address>
    suspend fun addAddress(address: Address): Address
    suspend fun updateAddress(address: Address): Address
    suspend fun deleteAddress(id: Int): Boolean
}

@RpcService
interface IProfileService {
    suspend fun getProfile(): Profile
}

@RpcService
interface IRegisterProfileService {
    suspend fun registerProfile(profile: Profile, password: String): Boolean
}
