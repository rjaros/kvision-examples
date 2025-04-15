package com.example

import io.kvision.Application
import io.kvision.BootstrapModule
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.Hot
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.panel.root
import io.kvision.panel.splitPanel
import io.kvision.remote.registerRemoteTypes
import io.kvision.startApplication
import io.kvision.utils.perc
import io.kvision.utils.useModule
import io.kvision.utils.vh
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

@JsModule("/kotlin/modules/css/kvapp.css")
external val kvappCss: dynamic

@JsModule("/kotlin/modules/i18n/messages-en.json")
external val messagesEn: dynamic

@JsModule("/kotlin/modules/i18n/messages-pl.json")
external val messagesPl: dynamic

class App : Application() {
    init {
        useModule(kvappCss)
    }

    override fun start() {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to messagesEn,
                    "pl" to messagesPl
                )
            )
        root("kvapp") {
            splitPanel {
                width = 100.perc
                height = 100.vh
                add(ListPanel)
                add(EditPanel)
            }
        }
        AppScope.launch {
            Model.getAddressList()
        }
    }
}

fun main() {
    registerRemoteTypes()
    startApplication(::App, js("import.meta.webpackHot").unsafeCast<Hot?>(), BootstrapModule, FontAwesomeModule, CoreModule)
}
