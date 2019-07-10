package com.example

import com.example.domain.EncodingType
import com.example.service.EncodingService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.core.*
import pl.treksoft.kvision.form.select.Select
import pl.treksoft.kvision.form.select.Select.Companion.select
import pl.treksoft.kvision.form.select.SelectInput.Companion.selectInput
import pl.treksoft.kvision.form.text.TextAreaInput.Companion.textAreaInput
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.Button
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.px

@ExperimentalCoroutinesApi
object App : ApplicationBase {

    private lateinit var root: Root
    private val service = EncodingService()

    override fun start(state: Map<String, Any>) {
        I18n.manager =
                DefaultI18nManager(
                        mapOf(
                                "en" to require("i18n/messages-en.json"),
                                "pl" to require("i18n/messages-pl.json")
                        )
                )
        root = Root("kvapp") {
            vPanel(spacing = 10) {
                margin = 30.px
                select(options = listOf("en" to "English", "pl" to "Polskie"), value = I18n.language, label = tr("Language")) {
                    setEventListener<Select> {
                        change = {
                            self.value?.let { I18n.language = it }
                        }
                    }
                }
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
                val encButton = Button(tr("Encode")).apply {
                    marginRight = 10.px
                }
                val decButton = Button(tr("Decode"))
                div {
                    add(encButton)
                    add(decButton)
                }
                val output = div {
                    padding = 5.px
                    border = Border(1.px, BorderStyle.SOLID, Col.BLACK)
                    height = 300.px
                    overflow = Overflow.AUTO
                    overflowWrap = OverflowWrap.BREAKWORK
                }
                encButton.onClick {
                    GlobalScope.launch {
                        val encodingType = select.value?.let { EncodingType.valueOf(it) } ?: EncodingType.BASE64
                        val result = service.encode(input.value ?: "", encodingType)
                        output.content = result
                    }
                }
                decButton.onClick {
                    GlobalScope.launch {
                        val encodingType = select.value?.let { EncodingType.valueOf(it) } ?: EncodingType.BASE64
                        val result = service.decode(input.value ?: "", encodingType)
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
