package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.Application
import pl.treksoft.kvision.core.AlignItems
import pl.treksoft.kvision.core.JustifyContent
import pl.treksoft.kvision.form.select.selectInput
import pl.treksoft.kvision.form.text.textInput
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.html.span
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.module
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px

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
