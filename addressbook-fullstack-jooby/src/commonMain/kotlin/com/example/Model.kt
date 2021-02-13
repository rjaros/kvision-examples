@file:UseContextualSerialization(LocalDateTime::class)

package com.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import io.kvision.types.LocalDateTime

@Serializable
data class Profile(
    val id: String? = null,
    val attributes: MutableMap<String, String> = mutableMapOf(),
    val authenticationAttributes: MutableMap<String, String> = mutableMapOf(),
    val roles: MutableSet<String> = mutableSetOf(),
    val permissions: MutableSet<String> = mutableSetOf(),
    val linkedId: String? = null,
    val remembered: Boolean = false,
    val clientName: String? = null
) {
    var username: String?
        get() = attributes["username"]
        set(value) {
            if (value != null) {
                attributes["username"] = value
            } else {
                attributes.remove("username")
            }
        }
    var firstName: String?
        get() = attributes["first_name"]
        set(value) {
            if (value != null) {
                attributes["first_name"] = value
            } else {
                attributes.remove("first_name")
            }
        }
    var familyName: String?
        get() = attributes["family_name"]
        set(value) {
            if (value != null) {
                attributes["family_name"] = value
            } else {
                attributes.remove("family_name")
            }
        }
    var displayName: String?
        get() = attributes["display_name"]
        set(value) {
            if (value != null) {
                attributes["display_name"] = value
            } else {
                attributes.remove("display_name")
            }
        }
    var email: String?
        get() = attributes["email"]
        set(value) {
            if (value != null) {
                attributes["email"] = value
            } else {
                attributes.remove("email")
            }
        }
    var pictureUrl: String?
        get() = attributes["picture_url"]
        set(value) {
            if (value != null) {
                attributes["picture_url"] = value
            } else {
                attributes.remove("picture_url")
            }
        }
    var profileUrl: String?
        get() = attributes["profile_url"]
        set(value) {
            if (value != null) {
                attributes["profile_url"] = value
            } else {
                attributes.remove("profile_url")
            }
        }
}

@Serializable
data class Address(
    val id: Int? = 0,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val postalAddress: String? = null,
    val favourite: Boolean? = false,
    val createdAt: LocalDateTime? = null,
    val userId: String? = null
)
