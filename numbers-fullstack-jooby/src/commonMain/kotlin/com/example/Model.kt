package com.example

import kotlinx.serialization.Serializable

@Serializable
enum class Language(val lang: String) {
    ENGLISH("English"),
    GERMAN("German"),
    RUSSIAN("Russian"),
    POLISH("Polish"),
    CZECH("Czech")
}
