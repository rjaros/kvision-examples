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
import io.kvision.utils.px
import kotlin.math.min

fun Container.toolbar(state: State) {
    form(className = "ui form") {
        hPanel(justify = JustifyContent.SPACEBETWEEN, wrap = FlexWrap.WRAP, noWrappers = true) {
            div(className = "fields") {
                div(className = "field") {
                    bulkSelect(state)
                }
                div(className = "field") {
                    searchBox(state)
                }
                div(className = "field") {
                    sortSelect(state)
                }
            }
            div(className = "fields") {
                pagination(state)
            }
        }
    }
}

fun Container.bulkSelect(state: State) {
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
                    if (this.value) Model.selectAll() else Model.selectNone()
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
                    Model.selectNone()
                }
                div("Select visible", className = "item").onClick {
                    Model.selectVisible()
                }
                div("Select all", className = "item").onClick {
                    Model.selectAll()
                }
            }
            addAfterInsertHook {
                @Suppress("UnsafeCastFromDynamic")
                getElementJQueryD().dropdown()
            }
        }
    }
}

fun Container.searchBox(state: State) {
    div(className = "ui icon input") {
        val input = textInput(value = state.search)
        i(className = "search link icon") {
            setAttribute("aria-hidden", "true")
        }.onClick {
            Model.search(input.value)
        }
    }
}

fun Container.sortSelect(state: State) {
    div(className = "ui floating dropdown icon button") {
        i(className = "sort amount down icon")
        div(className = "menu") {
            width = 150.px
            menuItem("Last name", state.sortItem == SortItem.LAST_NAME) {
                Model.setSortItem(SortItem.LAST_NAME)
            }
            menuItem("First name", state.sortItem == SortItem.FIRST_NAME) {
                Model.setSortItem(SortItem.FIRST_NAME)
            }
            menuItem("User name", state.sortItem == SortItem.USER_NAME) {
                Model.setSortItem(SortItem.USER_NAME)
            }
            menuItem("Age", state.sortItem == SortItem.AGE) {
                Model.setSortItem(SortItem.AGE)
            }
            menuItem("Nationality", state.sortItem == SortItem.NATIONALITY) {
                Model.setSortItem(SortItem.NATIONALITY)
            }
            div(className = "divider")
            menuItem("Ascending", state.sortType == SortType.ASC) {
                Model.setSortType(SortType.ASC)
            }
            menuItem("Descending", state.sortType == SortType.DESC) {
                Model.setSortType(SortType.DESC)
            }
        }
        addAfterInsertHook {
            @Suppress("UnsafeCastFromDynamic")
            getElementJQueryD().dropdown()
        }
    }
}

fun Container.pagination(state: State) {
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
                    Model.setPageSize(10)
                }
                menuItem("20 per page", state.perPage == 20) {
                    Model.setPageSize(20)
                }
                menuItem("50 per page", state.perPage == 50) {
                    Model.setPageSize(50)
                }
                menuItem("100 per page", state.perPage == 100) {
                    Model.setPageSize(100)
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
                    Model.prevPage()
                }
            }
            button("", "angle right icon", className = "ui icon button") {
                disabled = state.page == lastPage
                setAttribute("aria-label", "Go to next page")
                onClick {
                    Model.nextPage()
                }
            }
        }
    }
}
