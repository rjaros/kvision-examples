@file:UseContextualSerialization(OffsetDateTime::class)

package com.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import io.kvision.types.OffsetDateTime

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
expect annotation class Id()

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
expect annotation class Table(val value: String, val name: String, val schema: String)

@Serializable
data class Profile(
    val name: String? = null,
    val username: String? = null,
    val password: String? = null,
    val password2: String? = null
)

@Table("users", "users", "")
data class User(val id: Int? = null, val username: String, val password: String, val name: String)

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
