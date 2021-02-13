package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asDeferred
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import io.kvision.form.text.textInput
import io.kvision.html.button
import io.kvision.html.Div
import io.kvision.html.div
import io.kvision.html.setData
import io.kvision.i18n.I18n.tr
import io.kvision.panel.hPanel
import io.kvision.panel.SimplePanel
import io.kvision.panel.vPanel
import io.kvision.require
import io.kvision.rest.RestClient
import io.kvision.utils.px

@Serializable
data class Query(val q: String?)

@Serializable
data class SearchResult(val total_count: Int, val incomplete_results: Boolean)

class RestTab : SimplePanel() {

    val restClient = RestClient()

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
                            val searchResult = restClient.call<SearchResult, Query>(
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
