@file:UseContextualSerialization(LocalDateTime::class)

package com.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import io.kvision.types.LocalDateTime

@Serializable
data class Profile(
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
    val password: String? = null,
    val password2: String? = null
)

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
    val userId: Int? = null
)
