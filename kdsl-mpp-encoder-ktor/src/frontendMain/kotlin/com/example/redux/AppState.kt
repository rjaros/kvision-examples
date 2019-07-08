package com.example.redux

import com.example.EncodingType
import kotlinx.serialization.Serializable

@Serializable
data class AppState(
        val unencodedTextInput: String = "",
        val selectedEncodingType: EncodingType = EncodingType.URLENCODE,
        val encodedTextOutput: String = ""
)