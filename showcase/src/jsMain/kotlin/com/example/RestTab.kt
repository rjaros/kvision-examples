package com.example

import io.kvision.core.onClickLaunch
import io.kvision.form.text.textInput
import io.kvision.html.button
import io.kvision.i18n.I18n.tr
import io.kvision.ktml.KtmlTemplate
import io.kvision.ktml.ktmlTemplate
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel
import io.kvision.rest.RestClient
import io.kvision.rest.call
import io.kvision.utils.perc
import io.kvision.utils.px
import kotlinx.coroutines.asDeferred
import kotlinx.serialization.Serializable

@Serializable
data class Query(val q: String?)

@Serializable
data class SearchResult(val total_count: Int, val incomplete_results: Boolean)

class RestTab : SimplePanel() {

    val restClient = RestClient()

    init {
        this.marginTop = 10.px
        this.minHeight = 400.px

        lateinit var ktmlTemplate: KtmlTemplate

        vPanel(spacing = 20) {
            hPanel(spacing = 5) {
                val input = textInput {
                    width = 50.perc
                }
                button(tr("Search GitHub")).onClickLaunch {
                    input.value?.let {
                        val searchResult = this@RestTab.restClient.call<SearchResult, Query>(
                            "https://api.github.com/search/repositories",
                            Query(it)
                        ).asDeferred().await()
                        ktmlTemplate.parameters = mapOf("searchResult" to searchResult)
                    }
                }
            }
            ktmlTemplate = ktmlTemplate(mapOf("en" to "rest-en", "pl" to "rest-pl")) {
                fontSize = 20.px
            }
        }
    }
}
