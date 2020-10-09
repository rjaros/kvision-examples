package com.example

import kotlinx.browser.localStorage
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import pl.treksoft.kvision.state.ObservableValue

@Serializable
data class Address(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val favourite: Boolean? = false
)

fun Address.match(search: String?): Boolean {
    return search?.let {
        firstName?.contains(it, true) ?: false ||
                lastName?.contains(it, true) ?: false ||
                email?.contains(it, true) ?: false
    } ?: true
}

enum class Sort {
    FN, LN, E, F
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
    val sort: Sort = Sort.FN,
    val filter: Filter = Filter.ALL,
    val editMode: EditMode? = null,
    val editIndex: Int? = null,
    val editAddress: Address? = null
)

object Model {
    val addressBook = ObservableValue(
        AddressBookState(
            listOf(
                Address("John", "Smith", "john.smith@mail.com", true),
                Address("Karen", "Kowalsky", "kkowalsky@mail.com", true),
                Address("William", "Gordon", "w.gordon@mail.com", false)
            )
        )
    )

    fun setSort(sort: Sort) {
        addressBook.value = addressBook.value.copy(sort = sort)
    }

    fun setSearch(search: String?) {
        addressBook.value = addressBook.value.copy(search = search)
    }

    fun setFilter(filter: Filter) {
        addressBook.value = addressBook.value.copy(filter = filter)
    }

    fun add() {
        addressBook.value = addressBook.value.copy(editMode = EditMode.NEW, editIndex = null, editAddress = null)
    }

    fun edit(index: Int?) {
        val state = addressBook.value
        val editAddress = index?.let { state.addresses[it] }
        addressBook.value = state.copy(editMode = EditMode.EDIT, editIndex = index, editAddress = editAddress)
    }

    fun cancel() {
        addressBook.value = addressBook.value.copy(editMode = null, editIndex = null, editAddress = null)
    }

    fun delete(index: Int) {
        val state = addressBook.value
        val newAddresses = state.addresses.filterIndexed { i, _ -> i != index }
        val editIndex = state.editIndex ?: -1
        addressBook.value = if (editIndex == index) {
            state.copy(editMode = null, addresses = newAddresses, editIndex = null, editAddress = null)
        } else if (editIndex > index) {
            state.copy(addresses = newAddresses, editIndex = editIndex - 1)
        } else {
            state.copy(addresses = newAddresses)
        }
        storeAddresses()
    }

    fun save(newAddress: Address) {
        val state = addressBook.value
        val newAddresses = if (state.editMode == EditMode.EDIT) {
            state.addresses.mapIndexed { i, address ->
                if (i == state.editIndex) newAddress else address
            }
        } else {
            state.addresses + newAddress
        }
        addressBook.value = state.copy(addresses = newAddresses, editMode = null, editIndex = null, editAddress = null)
        storeAddresses()
    }

    fun storeAddresses() {
        val jsonString = Json.encodeToString(addressBook.value.addresses)
        localStorage["addresses"] = jsonString
    }

    fun loadAddresses() {
        localStorage["addresses"]?.let { addressesAsString ->
            addressBook.value = addressBook.value.copy(addresses = Json.decodeFromString(addressesAsString))
        }
    }
}
