package com.example

import dev.kilua.rpc.getService
import io.kvision.state.ObservableList
import io.kvision.state.ObservableValue
import io.kvision.state.observableListOf
import io.kvision.utils.syncWithList
import kotlinx.coroutines.launch

object Model {

    private val addressService = getService<IAddressService>()
    private val profileService = getService<IProfileService>()
    private val registerProfileService = getService<IRegisterProfileService>()

    val addresses: ObservableList<Address> = observableListOf()
    val profile = ObservableValue(Profile())

    var search: String? = null
        set(value) {
            field = value
            AppScope.launch {
                getAddressList()
            }
        }
    var types: String = "all"
        set(value) {
            field = value
            AppScope.launch {
                getAddressList()
            }
        }
    var sort = Sort.FN
        set(value) {
            field = value
            AppScope.launch {
                getAddressList()
            }
        }

    suspend fun getAddressList() {
        Security.withAuth {
            val newAddresses = addressService.getAddressList(search, types, sort)
            addresses.syncWithList(newAddresses)
        }
    }

    suspend fun addAddress(address: Address) {
        Security.withAuth {
            addressService.addAddress(address)
            getAddressList()
        }
    }

    suspend fun updateAddress(address: Address) {
        Security.withAuth {
            addressService.updateAddress(address)
            getAddressList()
        }
    }

    suspend fun deleteAddress(id: Int): Boolean {
        return Security.withAuth {
            val result = addressService.deleteAddress(id)
            getAddressList()
            result
        }
    }

    suspend fun readProfile() {
        Security.withAuth {
            profile.value = profileService.getProfile()
        }
    }

    suspend fun registerProfile(profile: Profile, password: String): Boolean {
        return try {
            registerProfileService.registerProfile(profile, password)
        } catch (e: Exception) {
            console.log(e)
            false
        }
    }
}
