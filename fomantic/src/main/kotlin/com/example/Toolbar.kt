package com.example

import io.kvision.core.Container
import io.kvision.core.FlexWrap
import io.kvision.core.JustifyContent
import io.kvision.core.onClick
import io.kvision.form.check.checkBoxInput
import io.kvision.form.form
import io.kvision.form.text.textInput
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.i
import io.kvision.html.label
import io.kvision.html.link
import io.kvision.panel.hPanel
import io.kvision.redux.Dispatch
import io.kvision.utils.px
import kotlin.math.min

fun Container.toolbar(state: State, dispatch: Dispatch<Action>) {
    form(className = "ui form") {
        hPanel(justify = JustifyContent.SPACEBETWEEN, wrap = FlexWrap.WRAP, noWrappers = true) {
            div(className = "fields") {
                div(className = "field") {
                    bulkSelect(state, dispatch)
                }
                div(className = "field") {
                    searchBox(state, dispatch)
                }
                div(className = "field") {
                    sortSelect(state, dispatch)
                }
            }
            div(className = "fields") {
                pagination(state, dispatch)
            }
        }
    }
}

fun Container.bulkSelect(state: State, dispatch: Dispatch<Action>) {
    div(className = "ui buttons") {
        div(className = "ui button") {
            checkBoxInput {
                id = "toolbar-selection-state"
                setAttribute("aria-labelledby", "toolbar-selection-state-info")
                when (state.selectionState.check) {
                    TriState.CHECKED -> {
                        value = true
                    }
                    TriState.UNCHECKED -> {
                        value = false
                    }
                    else -> {
                        addAfterInsertHook {
                            it.elm.asDynamic().indeterminate = true
                        }
                    }
                }
                onClick {
                    if (this.value) dispatch(Action.SelectAll) else dispatch(Action.SelectNone)
                }
            }
            +" "
            label(state.selectionState.info) {
                id = "toolbar-selection-state-info"
                setAttribute("aria-hidden", "true")
            }
        }
        div(className = "ui floating dropdown icon button") {
            i(className = "dropdown icon")
            div(className = "menu") {
                div("Select none", className = "item").onClick {
                    dispatch(Action.SelectNone)
                }
                div("Select visible", className = "item").onClick {
                    dispatch(Action.SelectVisible)
                }
                div("Select all", className = "item").onClick {
                    dispatch(Action.SelectAll)
                }
            }
            addAfterInsertHook {
                @Suppress("UnsafeCastFromDynamic")
                getElementJQueryD().dropdown()
            }
        }
    }
}

fun Container.searchBox(state: State, dispatch: Dispatch<Action>) {
    div(className = "ui icon input") {
        val input = textInput(value = state.search)
        i(className = "search link icon") {
            setAttribute("aria-hidden", "true")
        }.onClick {
            dispatch(Action.Search(input.value))
        }
    }
}

fun Container.sortSelect(state: State, dispatch: Dispatch<Action>) {
    div(className = "ui floating dropdown icon button") {
        i(className = "sort amount down icon")
        div(className = "menu") {
            width = 150.px
            menuItem("Last name", state.sortItem == SortItem.LAST_NAME) {
                dispatch(Action.Sort(SortItem.LAST_NAME, state.sortType))
            }
            menuItem("First name", state.sortItem == SortItem.FIRST_NAME) {
                dispatch(Action.Sort(SortItem.FIRST_NAME, state.sortType))
            }
            menuItem("User name", state.sortItem == SortItem.USER_NAME) {
                dispatch(Action.Sort(SortItem.USER_NAME, state.sortType))
            }
            menuItem("Age", state.sortItem == SortItem.AGE) {
                dispatch(Action.Sort(SortItem.AGE, state.sortType))
            }
            menuItem("Nationality", state.sortItem == SortItem.NATIONALITY) {
                dispatch(Action.Sort(SortItem.NATIONALITY, state.sortType))
            }
            div(className = "divider")
            menuItem("Ascending", state.sortType == SortType.ASC) {
                dispatch(Action.Sort(state.sortItem, SortType.ASC))
            }
            menuItem("Descending", state.sortType == SortType.DESC) {
                dispatch(Action.Sort(state.sortItem, SortType.DESC))
            }
        }
        addAfterInsertHook {
            @Suppress("UnsafeCastFromDynamic")
            getElementJQueryD().dropdown()
        }
    }
}

fun Container.pagination(state: State, dispatch: Dispatch<Action>) {
    val itemsFiltered = state.usersFiltered()
    val rowsInfo = "${(state.page - 1) * state.perPage + 1} - ${min(itemsFiltered.size, state.page * state.perPage)}"
    val lastPage = ((itemsFiltered.size - 1) / state.perPage) + 1

    div(className = "field") {
        div(className = "ui left labeled button") {
            link("$rowsInfo of ${itemsFiltered.size}", className = "ui basic label")
        }
        div(className = "ui floating dropdown icon button") {
            i(className = "caret down icon")
            div(className = "menu") {
                width = 150.px
                menuItem("10 per page", state.perPage == 10) {
                    dispatch(Action.SetPageSize(10))
                }
                menuItem("20 per page", state.perPage == 20) {
                    dispatch(Action.SetPageSize(20))
                }
                menuItem("50 per page", state.perPage == 50) {
                    dispatch(Action.SetPageSize(50))
                }
                menuItem("100 per page", state.perPage == 100) {
                    dispatch(Action.SetPageSize(100))
                }
            }
            addAfterInsertHook {
                @Suppress("UnsafeCastFromDynamic")
                getElementJQueryD().dropdown()
            }
        }
        div(className = "ui icon buttons") {
            button("", "angle left icon", className = "ui icon button") {
                disabled = state.page == 1
                setAttribute("aria-label", "Go to previous page")
                onClick {
                    dispatch(Action.PrevPage)
                }
            }
            button("", "angle right icon", className = "ui icon button") {
                disabled = state.page == lastPage
                setAttribute("aria-label", "Go to next page")
                onClick {
                    dispatch(Action.NextPage)
                }
            }
        }
    }
}
