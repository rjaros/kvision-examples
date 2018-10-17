package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.form.select.SelectInput.Companion.selectInput
import pl.treksoft.kvision.form.text.TextInput.Companion.textInput
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.Label.Companion.label
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.utils.px

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
object App : ApplicationBase {

    private val numberService = NumberService()

    private lateinit var root: Root
    private lateinit var outputDiv: Div

    override fun start(state: Map<String, Any>) {
        root = Root("kvapp") {
            vPanel(justify = FlexJustify.CENTER, alignItems = FlexAlignItems.CENTER, spacing = 50) {
                marginTop = 50.px
                hPanel(justify = FlexJustify.CENTER, alignItems = FlexAlignItems.CENTER, spacing = 20) {
                    label("Your number:")
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
                                    numberService.numberToWords(number, Language.valueOf(lang)).await()
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

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }
}
