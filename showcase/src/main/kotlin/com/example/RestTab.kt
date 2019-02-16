package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asDeferred
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import pl.treksoft.kvision.form.text.TextInput.Companion.textInput
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.setData
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.remote.CallAgent
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.px

@Serializable
data class Query(val q: String?)

@Serializable
data class SearchResult(val total_count: Int, val incomplete_results: Boolean)

class RestTab : SimplePanel() {

    val callAgent = CallAgent()

    init {
        this.marginTop = 10.px
        this.minHeight = 400.px

        lateinit var div: Div

        vPanel(spacing = 20) {
            hPanel(spacing = 5) {
                val input = textInput()
                button(tr("Search GitHub")).onClick {
                    GlobalScope.launch {
                        input.value?.let {
                            val searchResult = callAgent.call<SearchResult, Query>(
                                "https://api.github.com/search/repositories",
                                Query(it)
                            ).asDeferred().await()
                            div.setData(searchResult)
                        }
                    }
                }
            }
            div = div {
                fontSize = 20.px
                templates = mapOf(
                    "en" to require("hbs/rest.en.hbs"),
                    "pl" to require("hbs/rest.pl.hbs")
                )
            }
        }
    }
}
