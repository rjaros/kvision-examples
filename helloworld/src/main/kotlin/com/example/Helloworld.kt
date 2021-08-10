package com.example

import io.kvision.Application
import io.kvision.core.FlexDirection
import io.kvision.core.JustifyContent
import io.kvision.html.div
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.i18n.I18n.tr
import io.kvision.panel.flexPanel
import io.kvision.module
import io.kvision.panel.root
import io.kvision.require
import io.kvision.startApplication
import io.kvision.utils.px

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
                div(tr("Hello world!"), className = "helloworld") {
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
