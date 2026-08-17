package com.example

import io.kvision.Application
import io.kvision.Hot
import io.kvision.html.div
import io.kvision.panel.root
import io.kvision.startApplication
import io.kvision.utils.px
import io.kvision.utils.useModule
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.w3c.dom.Element

@JsModule("fomantic-ui-css/semantic.css")
external object semanticCss

@JsModule("fomantic-ui-css/semantic.js")
external object semanticUi

@JsModule("./modules/css/kvapp.css")
external object kvappCss

external fun jQuery(element: dynamic): dynamic

class App : Application(), CoroutineScope by CoroutineScope(Dispatchers.Default) {

    init {
        useModule(semanticCss)
        useModule(semanticUi)
        useModule(kvappCss)
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
    startApplication(::App, js("import.meta.webpackHot").unsafeCast<Hot?>())
}
