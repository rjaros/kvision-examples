package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import io.kvision.Application
import io.kvision.core.AlignItems
import io.kvision.core.JustifyContent
import io.kvision.form.select.selectInput
import io.kvision.form.text.textInput
import io.kvision.html.ButtonStyle
import io.kvision.html.Div
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.span
import io.kvision.panel.hPanel
import io.kvision.module
import io.kvision.panel.root
import io.kvision.panel.vPanel
import io.kvision.startApplication
import io.kvision.utils.perc
import io.kvision.utils.px

class App : Application() {

    private val numberService = NumberService()

    private lateinit var outputDiv: Div

    override fun start() {
        root("kvapp") {
            vPanel(justify = JustifyContent.CENTER, alignItems = AlignItems.CENTER, spacing = 50) {
                width = 100.perc
                marginTop = 50.px
                hPanel(justify = JustifyContent.CENTER, alignItems = AlignItems.CENTER, spacing = 20) {
                    span("Your number:")
                    val text = textInput {
                        width = 300.px
                    }
                    val languages = listOf(
                        "${Language.ENGLISH}" to Language.ENGLISH.lang,
                        "${Language.GERMAN}" to Language.GERMAN.lang,
                        "${Language.RUSSIAN}" to Language.RUSSIAN.lang,
                        "${Language.POLISH}" to Language.POLISH.lang,
                        "${Language.CZECH}" to Language.CZECH.lang
                    )
                    val languageSelect = selectInput(languages, "${Language.ENGLISH}")
                    button("Convert", style = ButtonStyle.PRIMARY).onClick {
                        GlobalScope.launch {
                            val result = text.value?.toIntOrNull()?.let { number ->
                                languageSelect.value?.let { lang ->
                                    numberService.numberToWords(number, Language.valueOf(lang))
                                }
                            }
                            outputDiv.content = result ?: "Not a valid number!"
                        }
                    }
                }
                outputDiv = div {
                    fontSize = 32.px
                }
            }
        }
    }
}

fun main() {
    startApplication(::App, module.hot)
}
