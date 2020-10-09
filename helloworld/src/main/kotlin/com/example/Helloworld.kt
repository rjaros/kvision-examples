package com.example

import pl.treksoft.kvision.Application
import pl.treksoft.kvision.core.FlexDirection
import pl.treksoft.kvision.core.JustifyContent
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.flexPanel
import pl.treksoft.kvision.module
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.require
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.utils.px

class Helloworld : Application() {
    init {
        require("css/helloworld.css")
    }

    override fun start() {

        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to require("i18n/messages-en.json"),
                    "pl" to require("i18n/messages-pl.json"),
                    "de" to require("i18n/messages-de.json"),
                    "es" to require("i18n/messages-es.json"),
                    "fr" to require("i18n/messages-fr.json"),
                    "ru" to require("i18n/messages-ru.json"),
                    "ja" to require("i18n/messages-ja.json"),
                    "ko" to require("i18n/messages-ko.json")
                )
            )

        root("helloworld") {
            flexPanel(FlexDirection.ROW, justify = JustifyContent.CENTER) {
                div(tr("Hello world!"), classes = setOf("helloworld")) {
                    marginTop = 50.px
                    fontSize = 50.px
                }
            }
        }
    }
}

fun main() {
    startApplication(::Helloworld, module.hot)
}
