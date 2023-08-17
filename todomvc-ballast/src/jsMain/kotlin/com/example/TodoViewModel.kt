package com.example

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.JsConsoleLogger
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.savedstate.BallastSavedStateInterceptor
import com.copperleaf.ballast.withViewModel
import com.example.MODE.ALL
import kotlinx.coroutines.CoroutineScope

class TodoViewModel(
    coroutineScope: CoroutineScope,
    todoInputHandler: TodoInputHandler,
    todoEventHandler: TodoEventHandler,
    todoSavedStateAdapter: TodoSavedStateAdapter
) : BasicViewModel<
        TodoContract.Inputs,
        TodoContract.Events,
        TodoContract.State>(
    config = BallastViewModelConfiguration.Builder().apply {
        this += LoggingInterceptor()
        logger = { JsConsoleLogger(it) }
        this += BallastSavedStateInterceptor(
            todoSavedStateAdapter
        )
    }.withViewModel(
        initialState = TodoContract.State(emptyList(), ALL),
        inputHandler = todoInputHandler,
        name = "TodoScreen",
    ).build(),
    eventHandler = todoEventHandler,
    coroutineScope = coroutineScope,
)
