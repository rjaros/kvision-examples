@file:UseContextualSerialization(LocalDateTime::class)

package com.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import pl.treksoft.kvision.types.LocalDateTime

expect class Profile

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
