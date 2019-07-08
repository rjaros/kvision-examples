package com.example.redux

import com.example.redux.AppAction.ChangeEncodedTextOutput
import com.example.redux.AppAction.ChangeEncodingType
import com.example.redux.AppAction.ChangeUnencodedTextInput

//object AppReducer {
fun reduce(state: AppState, action: AppAction): AppState {
    val newState = when (action) {
        is ChangeEncodedTextOutput -> state.copy(encodedTextOutput = action.encodedTextOutput)
        is ChangeEncodingType -> state.copy(selectedEncodingType = action.encodingType)
        is ChangeUnencodedTextInput -> state.copy(unencodedTextInput = action.unencodedTextInput)
    }
    println("Reduced. \n" +
            "Action: $action\n" +
            "Old State: $state\n" +
            "New State: $newState")
    return newState
}
//}