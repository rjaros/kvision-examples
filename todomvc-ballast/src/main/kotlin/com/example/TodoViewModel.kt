package com.example

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.JsConsoleBallastLogger
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.savedstate.BallastSavedStateInterceptor
import com.copperleaf.ballast.withViewModel
import com.example.MODE.ALL
import kotlinx.coroutines.CoroutineScope

class TodoViewModel(
    coroutineScope: CoroutineScope,
    todoSavedStateAdapter: TodoSavedStateAdapter
) : BasicViewModel<
        TodoContract.Inputs,
        TodoContract.Events,
        TodoContract.State>(
    config = BallastViewModelConfiguration.Builder().apply {
        this += LoggingInterceptor()
        logger = { JsConsoleBallastLogger(it) }
        this += BallastSavedStateInterceptor(
            todoSavedStateAdapter
        )
    }.withViewModel(
        initialState = TodoContract.State(emptyList(), ALL),
        inputHandler = TodoInputHandler(),
        name = "TodoScreen",
    ).build(),
    eventHandler = eventHandler {},
    coroutineScope = coroutineScope,
)
