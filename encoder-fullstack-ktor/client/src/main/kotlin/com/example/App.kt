package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.core.Border
import pl.treksoft.kvision.core.BorderStyle
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.Overflow
import pl.treksoft.kvision.core.OverflowWrap
import pl.treksoft.kvision.form.select.SelectInput.Companion.selectInput
import pl.treksoft.kvision.form.text.TextAreaInput.Companion.textAreaInput
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.px

object App : ApplicationBase {

    private lateinit var root: Root
    private val service = EncodingService()

    override fun start(state: Map<String, Any>) {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to require("./messages-en.json"),
                    "pl" to require("./messages-pl.json")
                )
            )
        root = Root("kvapp") {
            vPanel(spacing = 10) {
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
                    border = Border(1.px, BorderStyle.SOLID, Col.BLACK)
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

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }
}
