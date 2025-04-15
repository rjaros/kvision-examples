package com.example

import io.kvision.Application
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.Hot
import io.kvision.TailwindcssModule
import io.kvision.html.div
import io.kvision.html.image
import io.kvision.html.span
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.panel.root
import io.kvision.startApplication
import io.kvision.utils.useModule
import io.kvision.theme.ThemeManager
import io.kvision.theme.themeSwitcher
import io.kvision.utils.px
import io.kvision.utils.vh
import io.kvision.utils.vw

@JsModule("/kotlin/modules/css/kvapp.css")
external val kvappCss: dynamic

@JsModule("/kotlin/modules/i18n/messages-en.json")
external val messagesEn: dynamic

@JsModule("/kotlin/modules/i18n/messages-pl.json")
external val messagesPl: dynamic

@JsModule("/kotlin/modules/img/cat.jpg")
external val catJpg: dynamic

class App : Application() {
    init {
        ThemeManager.init()
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
            div(className = "bg-white dark:bg-black") {
                width = 100.vw
                height = 100.vh
                div(className = "flex flex-row justify-end gap-1 p-7") {
                    div(className = "dark:text-gray-400") {
                        +"Switch color theme:"
                    }
                    themeSwitcher("Switch theme", round = true) {
                        width = 24.px
                        height = 24.px
                    }
                }
                div(className = "flex items-center gap-6 p-7 flex-row gap-8 rounded-2xl") {
                    div {
                        image(catJpg, className = "size-48 shadow-xl rounded-md", alt = "")
                    }
                    div(className = "flex flex-col items-center") {
                        span(className = "text-2xl font-medium dark:text-gray-400") {
                            +"A nice cat"
                        }
                        span(className = "font-medium text-sky-500") {
                            +"Likes to play"
                        }
                        span(className = "flex gap-2 font-medium text-gray-600 dark:text-gray-400") {
                            span { +"Hello" }
                            span { +"·" }
                            span { +"KVision" }
                        }
                    }
                }
            }
        }
    }
}

fun main() {
    startApplication(::App, js("import.meta.webpackHot").unsafeCast<Hot?>(), TailwindcssModule, FontAwesomeModule, CoreModule)
}
