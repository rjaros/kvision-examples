package com.example

import kotlinx.serialization.Serializable

@Serializable
enum class MODE {
    ALL,
    ACTIVE,
    COMPLETED
}

@Serializable
data class Todo(val completed: Boolean, val title: String)
