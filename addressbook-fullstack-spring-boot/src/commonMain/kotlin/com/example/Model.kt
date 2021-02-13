@file:UseContextualSerialization(OffsetDateTime::class)

package com.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import io.kvision.remote.Id
import io.kvision.types.OffsetDateTime

expect class Profile

@Serializable
data class Address(
    @Id
    val id: Int? = 0,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val postalAddress: String? = null,
    val favourite: Boolean? = false,
    val createdAt: OffsetDateTime? = null,
    val userId: Int? = null
)
