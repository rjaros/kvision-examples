package com.example

import kotlinx.browser.localStorage
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import io.kvision.state.ObservableValue

@OptIn(ExperimentalJsExport::class)
@Serializable
@JsExport
data class Address(
    var id: Int? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var favourite: Boolean? = false
)

fun Address.match(search: String?): Boolean {
    return search?.let {
        firstName?.contains(it, true) ?: false ||
                lastName?.contains(it, true) ?: false ||
                email?.contains(it, true) ?: false
    } ?: true
}

enum class Filter {
    ALL,
    FAVOURITE
}

enum class EditMode {
    NEW,
    EDIT
}

data class AddressBookState(
    val addresses: List<Address>,
    val search: String? = null,
    val filter: Filter = Filter.ALL,
    val editMode: EditMode? = null,
    val editAddress: Address? = null
)

object Model {
    private var counter = 0

    val addressBook = ObservableValue(
        AddressBookState(
            listOf(
                Address(counter++, "John", "Smith", "john.smith@mail.com", true),
                Address(counter++, "Karen", "Kowalsky", "kkowalsky@mail.com", true),
                Address(counter++, "William", "Gordon", "w.gordon@mail.com", false)
            )
        )
    )

    fun setSearch(search: String?) {
        addressBook.value = addressBook.value.copy(search = search)
    }

    fun setFilter(filter: Filter) {
        addressBook.value = addressBook.value.copy(filter = filter)
    }

    fun add() {
        addressBook.value = addressBook.value.copy(editMode = EditMode.NEW, editAddress = null)
    }

    fun edit(id: Int) {
        val state = addressBook.value
        val editAddress = state.addresses.find { it.id == id }
        if (editAddress != null) {
            addressBook.value = state.copy(editMode = EditMode.EDIT, editAddress = editAddress)
        }
    }

    fun cancel() {
        addressBook.value = addressBook.value.copy(editMode = null, editAddress = null)
    }

    fun delete(id: Int) {
        val state = addressBook.value
        val newAddresses = state.addresses.filter { it.id != id }
        addressBook.value = if (state.editAddress?.id == id) {
            state.copy(editMode = null, addresses = newAddresses, editAddress = null)
        } else {
            state.copy(addresses = newAddresses)
        }
        storeAddresses()
    }

    fun save(newAddress: Address) {
        val state = addressBook.value
        val newAddresses = if (state.editMode == EditMode.EDIT) {
            state.addresses.map {
                if (it.id == state.editAddress?.id) newAddress.copy(id = it.id) else it
            }
        } else {
            state.addresses + newAddress.copy(id = counter++)
        }
        addressBook.value = state.copy(addresses = newAddresses, editMode = null, editAddress = null)
        storeAddresses()
    }

    fun storeAddresses() {
        val jsonString = Json.encodeToString(addressBook.value.addresses)
        localStorage["addressesTabulator"] = jsonString
    }

    fun loadAddresses() {
        localStorage["addressesTabulator"]?.let { addressesAsString ->
            addressBook.value = addressBook.value.copy(addresses = Json.decodeFromString(addressesAsString))
            counter = (addressBook.value.addresses.maxByOrNull { it.id ?: 0 }?.id ?: 0) + 1
        }
    }
}
