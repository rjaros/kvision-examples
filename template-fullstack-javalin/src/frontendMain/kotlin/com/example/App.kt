package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.Application
import pl.treksoft.kvision.html.Span
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.startApplication

class App : Application() {

    override fun start(state: Map<String, Any>) {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to pl.treksoft.kvision.require("i18n/messages-en.json"),
                    "pl" to pl.treksoft.kvision.require("i18n/messages-pl.json")
                )
            )
        val root = root("kvapp") {
        }
        GlobalScope.launch {
            val pingResult = Model.ping("Hello world from client!")
            root.add(Span(pingResult))
        }
    }
}

fun main() {
    startApplication(::App)
}
