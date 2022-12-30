package com.example

import io.kvision.Application
import io.kvision.html.div
import io.kvision.module
import io.kvision.panel.root
import io.kvision.require
import io.kvision.startApplication
import io.kvision.utils.px
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class App : Application(), CoroutineScope by CoroutineScope(Dispatchers.Default) {

    init {
        require("fomantic-ui-css/semantic.min.js")
        require("css/kvapp.css")
    }

    override fun start() {
        val stateFlow = MutableStateFlow(State())
        val actionFlow = MutableSharedFlow<Action>()
        launch {
            val users = randomUsers(1000).await()
            actionFlow.emit(Action.SetUsers(users))
        }
        launch {
            actionFlow.collect {
                stateFlow.value = stateReducer(stateFlow.value, it)
            }
        }
        root("kvapp") {
            padding = 10.px
            div(className = "ui fluid container") {
                div(className = "ui segment") {
                    toolbar(stateFlow, actionFlow)
                    cardView(stateFlow, actionFlow)
                }
            }
        }
    }
}

fun main() {
    startApplication(::App, module.hot)
}
