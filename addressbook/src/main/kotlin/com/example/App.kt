package com.example

import pl.treksoft.kvision.Application
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.module
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.panel.splitPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.vh

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
                height = 100.vh
                width = 100.perc
                listPanel()
                editPanel()
            }
        }
        Model.loadAddresses()
    }
}

fun main() {
    startApplication(::App, module.hot)
}
