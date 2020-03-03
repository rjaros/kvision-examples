package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.state.ObservableList
import pl.treksoft.kvision.state.observableListOf
import pl.treksoft.kvision.utils.syncWithList

object Model {

    private val addressService = AddressService()
    private val profileService = ProfileService()
    private val registerProfileService = RegisterProfileService()

    val addresses: ObservableList<Address> = observableListOf()
    val profile: ObservableList<Profile> = observableListOf(Profile())

    var search: String? = null
        set(value) {
            field = value
            GlobalScope.launch {
                getAddressList()
            }
        }
    var types: String = "all"
        set(value) {
            field = value
            GlobalScope.launch {
                getAddressList()
            }
        }
    var sort = Sort.FN
        set(value) {
            field = value
            GlobalScope.launch {
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
            profile[0] = profileService.getProfile()
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
