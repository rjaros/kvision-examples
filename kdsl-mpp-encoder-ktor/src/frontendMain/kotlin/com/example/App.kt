package com.example

import com.example.redux.AppAction.ChangeEncodedTextOutput
import com.example.redux.AppAction.ChangeEncodingType
import com.example.redux.AppAction.ChangeUnencodedTextInput
import com.example.redux.AppState
import com.example.redux.reduce
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.core.*
import pl.treksoft.kvision.form.select.Select
import pl.treksoft.kvision.form.select.SelectInput.Companion.selectInput
import pl.treksoft.kvision.form.text.TextAreaInput.Companion.textAreaInput
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.Span.Companion.span
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.redux.StateBinding.Companion.stateBinding
import pl.treksoft.kvision.redux.createReduxStore
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.px


object App : ApplicationBase {

    private val store = createReduxStore(::reduce, AppState())

    private lateinit var root: Root

    private val hammerjs = require("hammerjs")

    private val service = EncodingService()

    val css = require("css/kvapp.css")

    override fun start(state: Map<String, Any>) {
        I18n.manager = DefaultI18nManager(
                mapOf(
                        "en" to require("i18n/messages-en.json"),
                        "pl" to require("i18n/messages-pl.json")
                )
        )
        root = Root("kvapp") {
            vPanel(spacing = 10) {
                span(tr("Language")) {
                    margin = 10.px
                }
                selectInput(listOf("en" to "English", "pl" to "Polish"), I18n.language) {
                    verticalAlign = VerticalAlign.MIDDLE
                    setEventListener<Select> {
                        change = {
                            println("requested to change language to: ${self.value}")
                            I18n.language = self.value ?: "en"
                        }
                    }
                    margin = 10.px
                }
                vPanel(spacing = 10).stateBinding(store) { state ->
                    margin = 30.px
                    textAreaInput {
                        placeholder = tr("Enter some text")
                        height = 300.px
                        autofocus = true
                        value = state.unencodedTextInput
                        setEventListener {
                            change = { _ ->
                                value?.let { store.dispatch(ChangeUnencodedTextInput(it)) }
                            }
                        }
                    }
                    selectInput(
                            value = state.selectedEncodingType.name, options = listOf(
                            EncodingType.BASE64.name to tr("Base64"),
                            EncodingType.URLENCODE.name to tr("URL Encode"),
                            EncodingType.HEX.name to tr("Hex")
                    )
                    ) {
                        setEventListener {
                            change = { _ ->
                                value?.let { store.dispatch(ChangeEncodingType(EncodingType.valueOf(it))) }
                            }
                        }
                    }
                    button(tr("Encode")) {
                        onClick {
                            store.dispatch { dispatch, currentState ->
                                GlobalScope.launch {
                                    val result = service.encode(currentState().unencodedTextInput, currentState().selectedEncodingType)
                                    println(result)
                                    dispatch(ChangeEncodedTextOutput(result))
                                }
                            }
                        }
                    }
                    div(state.encodedTextOutput) {
                        padding = 5.px
                        border = Border(1.px, BorderStyle.SOLID, Col.BLACK)
                        height = 300.px
                        overflow = Overflow.AUTO
                        overflowWrap = OverflowWrap.BREAKWORK
                    }
                }
            }
        }

//        val hammerjs = hammerjs(document.body)
//        hammerjs.on("swiperight") {
//            store.dispatch(PokeAction.PrevPage)
//        }
//        hammerjs.on("swipeleft") {
//            store.dispatch(PokeAction.NextPage)
//        }
    }

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }
}
