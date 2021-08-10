package com.example

import io.kvision.Application
import io.kvision.BootstrapModule
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.module
import io.kvision.panel.root
import io.kvision.panel.splitPanel
import io.kvision.require
import io.kvision.startApplication
import io.kvision.utils.perc
import io.kvision.utils.vh
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

class App : Application() {
    init {
        require("css/kvapp.css")
    }

    override fun start() {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to require("i18n/messages-en.json"),
                    "pl" to require("i18n/messages-pl.json")
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
    startApplication(::App, module.hot, BootstrapModule, FontAwesomeModule, CoreModule)
}
