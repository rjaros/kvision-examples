package com.example

import dev.kilua.rpc.getService
import io.kvision.Application
import io.kvision.BootstrapCssModule
import io.kvision.BootstrapModule
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.Hot
import io.kvision.TomSelectModule
import io.kvision.core.Border
import io.kvision.core.BorderStyle
import io.kvision.core.Col
import io.kvision.core.Color
import io.kvision.core.Overflow
import io.kvision.core.OverflowWrap
import io.kvision.form.select.tomSelectInput
import io.kvision.form.text.textAreaInput
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.i18n.I18n.tr
import io.kvision.panel.root
import io.kvision.panel.vPanel
import io.kvision.remote.registerRemoteTypes
import io.kvision.startApplication
import io.kvision.utils.perc
import io.kvision.utils.px
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

@JsModule("./modules/i18n/messages-en.json")
external val messagesEn: dynamic

@JsModule("./modules/i18n/messages-pl.json")
external val messagesPl: dynamic

class App : Application() {

    private val service = getService<IEncodingService>()

    override fun start() {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to messagesEn,
                    "pl" to messagesPl
                )
            )
        root("kvapp") {
            vPanel(spacing = 10, useWrappers = true) {
                width = 100.perc
                padding = 30.px
                val input = textAreaInput {
                    placeholder = tr("Enter some text")
                    height = 300.px
                    autofocus = true
                }
                val select = tomSelectInput(
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
                    overflowWrap = OverflowWrap.BREAKWORD
                }
                button.onClick {
                    AppScope.launch {
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
    registerRemoteTypes()
    startApplication(
        ::App,
        js("import.meta.webpackHot").unsafeCast<Hot?>(),
        BootstrapModule,
        BootstrapCssModule,
        FontAwesomeModule,
        TomSelectModule,
        CoreModule
    )
}
