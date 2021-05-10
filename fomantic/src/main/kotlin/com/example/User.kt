/*
 * A modified data model from patterfly-fritz2-showcase demo project by Harald Pehl (https://github.com/hpehl)
 * published under the Apache-2.0 License
 *
 * https://github.com/patternfly-kotlin/patternfly-fritz2-showcase/blob/master/src/main/kotlin/org/patternfly/showcase/data/User.kt
 */

package com.example

import kotlinx.serialization.Serializable
import io.kvision.rest.RestClient
import kotlin.js.Promise

@Serializable
data class User(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val login: Login,
    val dob: DateOfBirth,
    val registered: DateOfBirth,
    val phone: String,
    val cell: String,
    val picture: Picture,
    val nat: String
) {
    fun match(query: String): Boolean = if (query.isEmpty()) true else {
        name.first.lowercase().contains(query.lowercase()) ||
                name.last.lowercase().contains(query.lowercase()) ||
                email.lowercase().contains(query.lowercase()) ||
                login.username.lowercase().contains(query.lowercase())
    }
}

@Serializable
data class Name(val title: String, val first: String, val last: String) {
    override fun toString(): String = "$first $last"
}

@Serializable
data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val coordinates: Coordinates,
    val timezone: Timezone
)

@Serializable
data class Street(val name: String, val number: Int)

@Serializable
data class Coordinates(val latitude: String, val longitude: String)

@Serializable
data class Timezone(val offset: String, val description: String)

@Serializable
data class Login(
    val uuid: String,
    val username: String,
    val password: String,
    val salt: String,
    val md5: String,
    val sha1: String,
    val sha256: String
)

@Serializable
data class DateOfBirth(val date: String, val age: Int)

@Serializable
data class Picture(val large: String, val medium: String, val thumbnail: String)

@Serializable
internal data class RandomUsers(val results: List<User>, val info: Info)

@Serializable
internal data class Info(val seed: String, val results: Int, val page: Int, val version: String)

fun randomUsers(size: Int = 123): Promise<List<User>> {
    val randomUsers = RestClient().call<RandomUsers>("https://randomuser.me/api/?exc=id&results=$size")
    return randomUsers.then { it.results }
}
