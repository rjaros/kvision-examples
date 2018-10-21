package com.example

import com.lightningkite.kotlin.observable.list.ObservableList
import com.lightningkite.kotlin.observable.list.observableListOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import pl.treksoft.kvision.remote.Profile
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
            GlobalScope.async {
                getAddressList()
            }
        }
    var types: String = "all"
        set(value) {
            field = value
            GlobalScope.async {
                getAddressList()
            }
        }
    var sort = Sort.FN
        set(value) {
            field = value
            GlobalScope.async {
                getAddressList()
            }
        }

    suspend fun getAddressList() {
        Security.withAuth {
            val newAddresses = addressService.getAddressList(search, types, sort).await()
            addresses.syncWithList(newAddresses)
        }
    }

    suspend fun addAddress(address: Address) {
        Security.withAuth {
            addressService.addAddress(address).await()
            getAddressList()
        }
    }

    suspend fun updateAddress(address: Address) {
        Security.withAuth {
            addressService.updateAddress(address).await()
            getAddressList()
        }
    }

    suspend fun deleteAddress(id: Int): Boolean {
        return Security.withAuth {
            val result = addressService.deleteAddress(id).await()
            getAddressList()
            result
        }
    }

    suspend fun readProfile() {
        Security.withAuth {
            profile[0] = profileService.getProfile().await()
        }
    }

    suspend fun registerProfile(profile: Profile, password: String): Boolean {
        return try {
            registerProfileService.registerProfile(profile, password).await()
        } catch (e: Exception) {
            console.log(e)
            false
        }
    }
}
