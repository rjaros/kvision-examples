package com.example

import kotlinx.browser.document
import kotlinx.serialization.builtins.ListSerializer
import pl.treksoft.kvision.Application
import pl.treksoft.kvision.core.AlignItems
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.core.JustifyContent
import pl.treksoft.kvision.core.JustifyItems
import pl.treksoft.kvision.core.TextAlign
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.form.text.text
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.gettext
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.module
import pl.treksoft.kvision.panel.gridPanel
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.redux.ActionCreator
import pl.treksoft.kvision.redux.createReduxStore
import pl.treksoft.kvision.require
import pl.treksoft.kvision.rest.RestClient
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.toolbar.buttonGroup
import pl.treksoft.kvision.utils.auto
import pl.treksoft.kvision.utils.obj
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px

class App : Application() {

    private val store = createReduxStore(::pokedexReducer, Pokedex(false, null, listOf(), listOf(), null, 0, 1))

    private val hammerjs = require("hammerjs")

    override fun start() {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "pl" to require("i18n/messages-pl.json"),
                    "en" to require("i18n/messages-en.json")
                )
            )

        root("kvapp") {
            vPanel(alignItems = AlignItems.STRETCH) {
                marginTop = 10.px
                width = 100.perc
                searchField()
                vPanel(store, alignItems = AlignItems.STRETCH) { state ->
                    maxWidth = 1200.px
                    textAlign = TextAlign.CENTER
                    marginLeft = auto
                    marginRight = auto
                    informationText(state)
                    if (!state.downloading && state.errorMessage == null) {
                        pokemonGrid(state)
                        pagination(state)
                    }
                }
            }
        }
        store.dispatch(downloadPokemons())
        val hammerjs = hammerjs(document.body)
        hammerjs.on("swiperight") {
            store.dispatch(PokeAction.PrevPage)
        }
        hammerjs.on("swipeleft") {
            store.dispatch(PokeAction.NextPage)
        }
    }

    private fun Container.searchField() {
        text {
            placeholder = tr("Enter pokemon name ...")
            width = 300.px
            marginLeft = auto
            marginRight = auto
            autofocus = true
            onEvent {
                input = {
                    store.dispatch(PokeAction.SetSearchString(self.value))
                }
            }
        }
    }

    private fun Container.informationText(state: Pokedex) {
        if (state.downloading) {
            div(tr("Loading ..."))
        } else if (state.errorMessage != null) {
            div(state.errorMessage)
        }
    }

    private fun Container.pokemonGrid(state: Pokedex) {
        gridPanel(
            templateColumns = "repeat(auto-fill, minmax(250px, 1fr))",
            justifyItems = JustifyItems.CENTER
        ) {
            state.visiblePokemons.forEach {
                add(PokeBox(it))
            }
        }
    }

    private fun Container.pagination(state: Pokedex) {
        hPanel(justify = JustifyContent.CENTER) {
            margin = 30.px
            buttonGroup {
                button("<<") {
                    disabled = state.pageNumber == 0
                    onClick {
                        store.dispatch(PokeAction.PrevPage)
                    }
                }
                button(" ${state.pageNumber + 1} / ${state.numberOfPages} ", disabled = true)
                button(">>") {
                    disabled = state.pageNumber == (state.numberOfPages - 1)
                    onClick {
                        store.dispatch(PokeAction.NextPage)
                    }
                }
            }
        }
    }

    private fun downloadPokemons(): ActionCreator<dynamic, Pokedex> {
        return { dispatch, _ ->
            val restClient = RestClient()
            dispatch(PokeAction.StartDownload)
            restClient.remoteCall(
                "https://pokeapi.co/api/v2/pokemon/", obj { limit = 800 },
                deserializer = ListSerializer(Pokemon.serializer())
            ) {
                it.results
            }.then { list ->
                dispatch(PokeAction.DownloadOk)
                dispatch(PokeAction.SetPokemonList(list))
                dispatch(PokeAction.SetSearchString(null))
            }.catch { e ->
                val info = if (!e.message.isNullOrBlank()) {
                    " (${e.message})"
                } else {
                    ""
                }
                dispatch(PokeAction.DownloadError(gettext("Service error!") + info))
            }
        }
    }
}

fun main() {
    startApplication(::App, module.hot)
}
