package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.Application
import pl.treksoft.kvision.core.Border
import pl.treksoft.kvision.core.BorderStyle
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.Color
import pl.treksoft.kvision.core.Overflow
import pl.treksoft.kvision.core.OverflowWrap
import pl.treksoft.kvision.form.select.selectInput
import pl.treksoft.kvision.form.text.textAreaInput
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px

class App : Application() {

    private val service = EncodingService()

    override fun start() {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to require("i18n/messages-en.json"),
                    "pl" to require("i18n/messages-pl.json")
                )
            )
        root("kvapp") {
            vPanel(spacing = 10) {
                width = 100.perc
                margin = 30.px
                val input = textAreaInput {
                    placeholder = tr("Enter some text")
                    height = 300.px
                    autofocus = true
                }
                val select = selectInput(
                    value = EncodingType.BASE64.name, options = listOf(
                        EncodingType.BASE64.name to tr("Base64"),
                        EncodingType.URLENCODE.name to tr("URL Encode"),
                        EncodingType.HEX.name to tr("Hex")
                    )
                )
                val button = button(tr("Encode"))
                val output = div {
                    padding = 5.px
                    border = Border(1.px, BorderStyle.SOLID, Color.name(Col.BLACK))
                    height = 300.px
                    overflow = Overflow.AUTO
                    overflowWrap = OverflowWrap.BREAKWORK
                }
                button.onClick {
                    GlobalScope.launch {
                        val encodingType = select.value?.let { EncodingType.valueOf(it) } ?: EncodingType.BASE64
                        val result = service.encode(input.value ?: "", encodingType)
                        output.content = result
                    }
                }
            }
        }
    }
}

fun main() {
    startApplication(::App)
}
