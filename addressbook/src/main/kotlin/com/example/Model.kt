package com.example

import kotlinx.browser.localStorage
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.w3c.dom.get
import org.w3c.dom.set
import pl.treksoft.kvision.state.ObservableList
import pl.treksoft.kvision.state.observableListOf

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

object Model {
    val addresses: ObservableList<Address> = observableListOf(
        Address("John", "Smith", "john.smith@mail.com", true),
        Address("Karen", "Kowalsky", "kkowalsky@mail.com", true),
        Address("William", "Gordon", "w.gordon@mail.com", false)
    )

    fun storeAddresses() {
        val jsonString = Json.encodeToString(ListSerializer(Address.serializer()), addresses)
        localStorage["addresses"] = jsonString
    }

    fun loadAddresses() {
        localStorage["addresses"]?.let { address ->
            addresses.clear()
            Json.decodeFromString(ListSerializer(Address.serializer()), address).forEach {
                addresses.add(it)
            }
        }
    }
}
