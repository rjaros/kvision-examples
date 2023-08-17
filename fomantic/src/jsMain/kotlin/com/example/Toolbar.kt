package com.example

import io.kvision.core.Container
import io.kvision.core.FlexWrap
import io.kvision.core.JustifyContent
import io.kvision.core.getElementJQueryD
import io.kvision.core.onClickLaunch
import io.kvision.form.check.checkBoxInput
import io.kvision.form.form
import io.kvision.form.text.textInput
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.i
import io.kvision.html.label
import io.kvision.html.link
import io.kvision.panel.hPanel
import io.kvision.state.bind
import io.kvision.utils.px
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.min

fun Container.toolbar(stateFlow: StateFlow<State>, actionFlow: MutableSharedFlow<Action>) {
    form(className = "ui form") {
        hPanel(justify = JustifyContent.SPACEBETWEEN, wrap = FlexWrap.WRAP) {
            div(className = "fields") {
                div(className = "field") {
                    bulkSelect(stateFlow, actionFlow)
                }
                div(className = "field") {
                    searchBox(stateFlow, actionFlow)
                }
                div(className = "field") {
                    sortSelect(stateFlow, actionFlow)
                }
            }
            div(className = "fields") {
                pagination(stateFlow, actionFlow)
            }
        }
    }
}

fun Container.bulkSelect(stateFlow: StateFlow<State>, actionFlow: MutableSharedFlow<Action>) {
    div(className = "ui buttons").bind(stateFlow, { it.selectionState }) { selectionState ->
        div(className = "ui button") {
            checkBoxInput {
                id = "toolbar-selection-state"
                setAttribute("aria-labelledby", "toolbar-selection-state-info")
                when (selectionState.check) {
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
                onClickLaunch {
                    if (this.value) actionFlow.emit(Action.SelectAll) else actionFlow.emit(Action.SelectNone)
                }
            }
            +" "
            label(selectionState.info) {
                id = "toolbar-selection-state-info"
                setAttribute("aria-hidden", "true")
            }
        }
        div(className = "ui floating dropdown icon button") {
            i(className = "dropdown icon")
            div(className = "menu") {
                div("Select none", className = "item").onClickLaunch {
                    actionFlow.emit(Action.SelectNone)
                }
                div("Select visible", className = "item").onClickLaunch {
                    actionFlow.emit(Action.SelectVisible)
                }
                div("Select all", className = "item").onClickLaunch {
                    actionFlow.emit(Action.SelectAll)
                }
            }
            addAfterInsertHook {
                @Suppress("UnsafeCastFromDynamic")
                getElementJQueryD().dropdown()
            }
        }
    }
}

fun Container.searchBox(stateFlow: StateFlow<State>, actionFlow: MutableSharedFlow<Action>) {
    div(className = "ui icon input") {
        val input = textInput().bind(stateFlow, { it.search }) { value = it }
        i(className = "search link icon") {
            setAttribute("aria-hidden", "true")
        }.onClickLaunch {
            actionFlow.emit(Action.Search(input.value))
        }
    }
}

fun Container.sortSelect(stateFlow: StateFlow<State>, actionFlow: MutableSharedFlow<Action>) {
    div(className = "ui floating dropdown icon button") {
        i(className = "sort amount down icon")
        div(className = "menu").bind(stateFlow, { it.sortItem to it.sortType }) {
            width = 150.px
            menuItem("Last name", it.first == SortItem.LAST_NAME) {
                actionFlow.emit(Action.Sort(SortItem.LAST_NAME, it.second))
            }
            menuItem("First name", it.first == SortItem.FIRST_NAME) {
                actionFlow.emit(Action.Sort(SortItem.FIRST_NAME, it.second))
            }
            menuItem("User name", it.first == SortItem.USER_NAME) {
                actionFlow.emit(Action.Sort(SortItem.USER_NAME, it.second))
            }
            menuItem("Age", it.first == SortItem.AGE) {
                actionFlow.emit(Action.Sort(SortItem.AGE, it.second))
            }
            menuItem("Nationality", it.first == SortItem.NATIONALITY) {
                actionFlow.emit(Action.Sort(SortItem.NATIONALITY, it.second))
            }
            div(className = "divider")
            menuItem("Ascending", it.second == SortType.ASC) {
                actionFlow.emit(Action.Sort(it.first, SortType.ASC))
            }
            menuItem("Descending", it.second == SortType.DESC) {
                actionFlow.emit(Action.Sort(it.first, SortType.DESC))
            }
        }
        addAfterInsertHook {
            @Suppress("UnsafeCastFromDynamic")
            getElementJQueryD().dropdown()
        }
    }
}

fun Container.pagination(stateFlow: StateFlow<State>, actionFlow: MutableSharedFlow<Action>) {
    div(className = "field").bind(stateFlow) { state ->
        val itemsFiltered = state.usersFiltered()
        val rowsInfo =
            "${(state.page - 1) * state.perPage + 1} - ${min(itemsFiltered.size, state.page * state.perPage)}"
        val lastPage = ((itemsFiltered.size - 1) / state.perPage) + 1

        div(className = "ui left labeled button") {
            link("$rowsInfo of ${itemsFiltered.size}", className = "ui basic label")
        }
        div(className = "ui floating dropdown icon button") {
            i(className = "caret down icon")
            div(className = "menu") {
                width = 150.px
                menuItem("10 per page", state.perPage == 10) {
                    actionFlow.emit(Action.SetPageSize(10))
                }
                menuItem("20 per page", state.perPage == 20) {
                    actionFlow.emit(Action.SetPageSize(20))
                }
                menuItem("50 per page", state.perPage == 50) {
                    actionFlow.emit(Action.SetPageSize(50))
                }
                menuItem("100 per page", state.perPage == 100) {
                    actionFlow.emit(Action.SetPageSize(100))
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
                onClickLaunch {
                    actionFlow.emit(Action.PrevPage)
                }
            }
            button("", "angle right icon", className = "ui icon button") {
                disabled = state.page == lastPage
                setAttribute("aria-label", "Go to next page")
                onClickLaunch {
                    actionFlow.emit(Action.NextPage)
                }
            }
        }
    }
}
