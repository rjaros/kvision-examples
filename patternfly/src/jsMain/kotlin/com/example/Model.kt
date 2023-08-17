package com.example

import io.kvision.redux.createTypedReduxStore

object Model {
    val store = createTypedReduxStore(::reducer, State())

    fun setUsers(users: List<User>) {
        store.dispatch(Action.SetUsers(users))
    }

    fun selectNone() {
        store.dispatch(Action.SelectNone)
    }

    fun selectVisible() {
        store.dispatch(Action.SelectVisible)
    }

    fun selectAll() {
        store.dispatch(Action.SelectAll)
    }

    fun selectUser(select: Boolean, uuid: String) {
        if (select) {
            store.dispatch(Action.SelectUser(uuid))
        } else {
            store.dispatch(Action.DeSelectUser(uuid))
        }
    }

    fun search(search: String?) {
        store.dispatch(Action.Search(search))
    }

    fun setSortItem(sortItem: SortItem) {
        store.dispatch(Action.Sort(sortItem, store.getState().sortType))
    }

    fun setSortType(sortType: SortType) {
        store.dispatch(Action.Sort(store.getState().sortItem, sortType))
    }

    fun setView(viewType: ViewType) {
        store.dispatch(Action.View(viewType))
    }

    fun setPageSize(perPage: Int) {
        store.dispatch(Action.SetPageSize(perPage))
    }

    fun nextPage() {
        store.dispatch(Action.NextPage)
    }

    fun prevPage() {
        store.dispatch(Action.PrevPage)
    }
}
