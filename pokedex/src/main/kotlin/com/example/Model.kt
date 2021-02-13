package com.example

import kotlinx.serialization.Serializable
import io.kvision.redux.RAction

const val MAX_ON_PAGE = 12

@Serializable
data class Pokemon(val name: String, val url: String)

@Serializable
data class Pokedex(
    val downloading: Boolean,
    val errorMessage: String?,
    val pokemons: List<Pokemon>,
    val visiblePokemons: List<Pokemon>,
    val searchString: String?,
    val pageNumber: Int,
    val numberOfPages: Int
)

sealed class PokeAction : RAction {
    object StartDownload : PokeAction()
    object DownloadOk : PokeAction()
    data class DownloadError(val errorMessage: String) : PokeAction()
    data class SetPokemonList(val pokemons: List<Pokemon>) : PokeAction()
    data class SetSearchString(val searchString: String?) : PokeAction()
    object NextPage : PokeAction()
    object PrevPage : PokeAction()
}

fun List<Pokemon>.filterBySearchString(searchString: String?): List<Pokemon> {
    return searchString?.let { search ->
        this.filter {
            it.name.toLowerCase().contains(search.toLowerCase())
        }
    } ?: this
}

fun List<Pokemon>.subListByPageNumber(pageNumber: Int): List<Pokemon> {
    return this.subList((pageNumber) * MAX_ON_PAGE, minOf((pageNumber + 1) * MAX_ON_PAGE, this.size))
}

fun pokedexReducer(state: Pokedex, action: PokeAction): Pokedex = when (action) {
    is PokeAction.StartDownload -> state.copy(downloading = true)
    is PokeAction.DownloadOk -> state.copy(downloading = false)
    is PokeAction.DownloadError -> state.copy(downloading = false, errorMessage = action.errorMessage)
    is PokeAction.SetPokemonList -> state.copy(pokemons = action.pokemons)
    is PokeAction.SetSearchString -> {
        val filteredPokemon = state.pokemons.filterBySearchString(action.searchString)
        val visiblePokemons = filteredPokemon.take(MAX_ON_PAGE)
        state.copy(
            visiblePokemons = visiblePokemons,
            searchString = action.searchString,
            pageNumber = 0,
            numberOfPages = ((filteredPokemon.size - 1) / MAX_ON_PAGE) + 1
        )
    }
    is PokeAction.NextPage -> if (state.pageNumber < state.numberOfPages - 1) {
        val newPageNumber = state.pageNumber + 1
        val visiblePokemons = state.pokemons.filterBySearchString(state.searchString).subListByPageNumber(newPageNumber)
        state.copy(visiblePokemons = visiblePokemons, pageNumber = newPageNumber)
    } else {
        state
    }
    is PokeAction.PrevPage -> if (state.pageNumber > 0) {
        val newPageNumber = state.pageNumber - 1
        val visiblePokemons = state.pokemons.filterBySearchString(state.searchString).subListByPageNumber(newPageNumber)
        state.copy(visiblePokemons = visiblePokemons, pageNumber = newPageNumber)
    } else {
        state
    }
}
