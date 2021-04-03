package com.example

import kotlin.math.min

enum class TriState {
    CHECKED,
    UNCHECKED,
    INDETERMINATE
}

enum class SortItem {
    LAST_NAME,
    FIRST_NAME,
    USER_NAME,
    AGE,
    NATIONALITY
}

enum class SortType {
    ASC,
    DESC
}

data class SelectionState(val check: TriState = TriState.UNCHECKED, val info: String? = null)

data class State(
    val selectionState: SelectionState = SelectionState(),
    val perPage: Int = 10,
    val page: Int = 1,
    val search: String? = null,
    val sortItem: SortItem? = SortItem.LAST_NAME,
    val sortType: SortType = SortType.ASC,
    val selected: Set<String> = emptySet(),
    val users: List<User> = emptyList(),
) {
    fun usersFiltered() =
        users.filter {
            if (this.search != null) {
                it.match(this.search)
            } else true
        }

    fun usersVisible(): List<User> {
        val usersFiltered = usersFiltered()
        return usersFiltered.subList((this.page - 1) * this.perPage, min(this.page * this.perPage, usersFiltered.size))
    }
}

sealed class Action {
    data class SetUsers(val users: List<User>) : Action()
    object SelectNone : Action()
    object SelectVisible : Action()
    object SelectAll : Action()
    data class SelectUser(val uuid: String) : Action()
    data class DeSelectUser(val uuid: String) : Action()
    data class Search(val search: String?) : Action()
    data class Sort(val sortItem: SortItem?, val SortType: SortType) : Action()
    data class SetPageSize(val perPage: Int) : Action()
    object NextPage : Action()
    object PrevPage : Action()
}


fun stateReducer(state: State, action: Action): State = when (action) {
    is Action.SetUsers -> {
        state.copy(users = action.users)
    }
    is Action.SelectNone -> {
        state.copy(
            selectionState = SelectionState(TriState.UNCHECKED, null),
            selected = emptySet()
        )
    }
    is Action.SelectVisible -> {
        val usersFiltered = state.usersFiltered().map { it.login.uuid }.toSet()
        val selected = state.usersVisible().map { it.login.uuid }.toSet()
        val selectionState = selectedState(selected, usersFiltered)
        state.copy(
            selectionState = selectionState,
            selected = selected
        )
    }
    is Action.SelectAll -> {
        val selected = state.usersFiltered().map { it.login.uuid }.toSet()
        state.copy(
            selectionState = SelectionState(TriState.CHECKED, "${selected.size} selected"),
            selected = selected
        )
    }
    is Action.SelectUser -> {
        val usersFiltered = state.usersFiltered().map { it.login.uuid }.toSet()
        val selected = state.selected + action.uuid
        val selectionState = selectedState(selected, usersFiltered)
        state.copy(
            selectionState = selectionState,
            selected = selected
        )
    }
    is Action.DeSelectUser -> {
        val usersFiltered = state.usersFiltered().map { it.login.uuid }.toSet()
        val selected = state.selected - action.uuid
        val selectionState = selectedState(selected, usersFiltered)
        state.copy(
            selectionState = selectionState,
            selected = selected
        )
    }
    is Action.Search -> {
        val usersFiltered = state.copy(search = action.search).usersFiltered().map { it.login.uuid }.toSet()
        val lastPage = ((usersFiltered.size - 1) / state.perPage) + 1
        val selectionState = selectedState(state.selected, usersFiltered)
        state.copy(
            selectionState = selectionState,
            search = action.search,
            page = min(state.page, lastPage)
        )
    }
    is Action.Sort -> {
        action.sortItem?.let { _ ->
            val sortedItems = when (action.SortType) {
                SortType.ASC -> {
                    state.users.sortedBy { it.sortSelector(action.sortItem) }
                }
                SortType.DESC -> {
                    state.users.sortedByDescending { it.sortSelector(action.sortItem) }
                }
            }
            state.copy(sortItem = action.sortItem, sortType = action.SortType, users = sortedItems)
        } ?: state
    }
    is Action.SetPageSize -> {
        if (action.perPage == 10 || action.perPage == 20 || action.perPage == 50 || action.perPage == 1000) {
            val lastPage = ((state.usersFiltered().size - 1) / action.perPage) + 1
            state.copy(perPage = action.perPage, page = min(state.page, lastPage))
        } else {
            state
        }
    }
    is Action.NextPage -> {
        val lastPage = ((state.usersFiltered().size - 1) / state.perPage) + 1
        if (state.page < lastPage) {
            state.copy(page = state.page + 1)
        } else {
            state
        }
    }
    is Action.PrevPage -> {
        if (state.page > 1) {
            state.copy(page = state.page - 1)
        } else {
            state
        }
    }
}

fun selectedState(selected: Set<String>, usersFiltered: Set<String>): SelectionState {
    return when {
        selected.isEmpty() -> SelectionState(TriState.UNCHECKED, null)
        selected.size == usersFiltered.size -> SelectionState(TriState.CHECKED, "${selected.size} selected")
        else -> SelectionState(TriState.INDETERMINATE, "${selected.size} selected")
    }
}

fun User.sortSelector(sortItem: SortItem): String {
    return when (sortItem) {
        SortItem.LAST_NAME -> this.name.last
        SortItem.FIRST_NAME -> this.name.first
        SortItem.USER_NAME -> this.login.username
        SortItem.AGE -> this.dob.age.toString().padStart(3, '0')
        SortItem.NATIONALITY -> this.nat
    }
}
