package com.example.redux

import com.example.EncodingType
import redux.RAction

sealed class AppAction : RAction {
    data class ChangeEncodedTextOutput(val encodedTextOutput: String) : AppAction()
    data class ChangeEncodingType(val encodingType: EncodingType) : AppAction()
    data class ChangeUnencodedTextInput(val unencodedTextInput: String) : AppAction()
}