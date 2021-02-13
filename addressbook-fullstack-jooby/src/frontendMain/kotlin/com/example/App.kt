package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import io.kvision.Application
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.module
import io.kvision.panel.root
import io.kvision.panel.splitPanel
import io.kvision.require
import io.kvision.startApplication
import io.kvision.utils.perc
import io.kvision.utils.vh

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
        GlobalScope.launch {
            Model.getAddressList()
        }
    }
}

fun main() {
    startApplication(::App, module.hot)
}
