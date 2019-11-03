@file:ContextualSerialization(OffsetDateTime::class)

package com.example

import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import pl.treksoft.kvision.remote.Id
import pl.treksoft.kvision.types.OffsetDateTime

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
