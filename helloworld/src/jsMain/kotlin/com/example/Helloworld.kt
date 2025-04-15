package com.example

import io.kvision.Application
import io.kvision.Hot
import io.kvision.core.FlexDirection
import io.kvision.core.JustifyContent
import io.kvision.html.div
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.i18n.I18n.tr
import io.kvision.panel.flexPanel
import io.kvision.panel.root
import io.kvision.startApplication
import io.kvision.utils.px
import io.kvision.utils.useModule

@JsModule("/kotlin/modules/css/helloworld.css")
external val helloworldCss: dynamic

@JsModule("/kotlin/modules/i18n/messages-en.json")
external val messagesEn: dynamic

@JsModule("/kotlin/modules/i18n/messages-pl.json")
external val messagesPl: dynamic

@JsModule("/kotlin/modules/i18n/messages-de.json")
external val messagesDe: dynamic

@JsModule("/kotlin/modules/i18n/messages-es.json")
external val messagesEs: dynamic

@JsModule("/kotlin/modules/i18n/messages-fr.json")
external val messagesFr: dynamic

@JsModule("/kotlin/modules/i18n/messages-ru.json")
external val messagesRu: dynamic

@JsModule("/kotlin/modules/i18n/messages-ja.json")
external val messagesJa: dynamic

@JsModule("/kotlin/modules/i18n/messages-ko.json")
external val messagesKo: dynamic

class Helloworld(private val rootid: String = "helloworld") : Application() {
    init {
        useModule(helloworldCss)
    }

    override fun start() {

        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to messagesEn,
                    "pl" to messagesPl,
                    "de" to messagesDe,
                    "es" to messagesEs,
                    "fr" to messagesFr,
                    "ru" to messagesRu,
                    "ja" to messagesJa,
                    "ko" to messagesKo
                )
            )

        root(rootid) {
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
    startApplication(
        ::Helloworld,
        js("import.meta.webpackHot").unsafeCast<Hot?>()
    )
}
