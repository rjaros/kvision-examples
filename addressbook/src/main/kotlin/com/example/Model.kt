package com.example

import com.lightningkite.kotlin.observable.list.ObservableList
import com.lightningkite.kotlin.observable.list.observableListOf
import org.w3c.dom.get
import org.w3c.dom.set
import pl.treksoft.kvision.form.asJson
import pl.treksoft.kvision.form.asMap
import kotlin.browser.localStorage
import kotlin.js.Json

data class Address(val map: Map<String, Any?>) {
    val firstName: String? by map
    val lastName: String? by map
    val email: String? by map
    val favourite: Boolean? by map
}

fun Address.match(search: String?): Boolean {
    return search?.let {
        firstName?.contains(it, true) ?: false ||
                lastName?.contains(it, true) ?: false ||
                email?.contains(it, true) ?: false
    } ?: true
}

object Model {

    val addresses: ObservableList<Address> = observableListOf(
        Address(
            mapOf(
                "firstName" to "John",
                "lastName" to "Smith",
                "email" to "john.smith@mail.com",
                "favourite" to true
            )
        ),
        Address(
            mapOf(
                "firstName" to "Karen",
                "lastName" to "Kowalsky",
                "email" to "kkowalsky@mail.com",
                "favourite" to true
            )
        ),
        Address(
            mapOf(
                "firstName" to "William",
                "lastName" to "Gordon",
                "email" to "w.gordon@mail.com",
                "favourite" to false
            )
        )
    )

    fun storeAddresses() {
        val jsonString = addresses.map {
            JSON.stringify(it.map.asJson())
        }.toString()
        localStorage["addresses"] = jsonString
    }

    fun loadAddresses() {
        localStorage["addresses"]?.let {
            addresses.clear()
            JSON.parse<Array<Json>>(it).forEach {
                addresses.add(Address(it.asMap()))
            }
        }
    }
}
