package com.example

import io.kvision.core.Container
import io.kvision.form.check.checkBoxInput
import io.kvision.form.text.TextInputType
import io.kvision.form.text.textInput
import io.kvision.html.*
import kotlin.math.min

fun Container.toolbar(state: State) {
    div(className = "pf-c-toolbar") {
        div(className = "pf-c-toolbar__content") {
            div(className = "pf-c-toolbar__content-section") {
                div(className = "pf-c-toolbar__item") {
                    bulkSelect(state)
                }
                div(className = "pf-c-toolbar__item") {
                    searchBox(state)
                }
                div(className = "pf-c-toolbar__item") {
                    sortSelect(state)
                }
                div(className = "pf-c-toolbar__group") {
                    div(className = "pf-c-toolbar__item") {
                        button("", icon = "fas fa-address-card", className = "pf-c-button pf-m-plain").onClick {
                            Model.setView(ViewType.CARD)
                        }
                    }
                    div(className = "pf-c-toolbar__item") {
                        button("", icon = "fas fa-list", className = "pf-c-button pf-m-plain").onClick {
                            Model.setView(ViewType.LIST)
                        }
                    }
                    div(className = "pf-c-toolbar__item") {
                        button("", icon = "fas fa-table", className = "pf-c-button pf-m-plain").onClick {
                            Model.setView(ViewType.TABLE)
                        }
                    }
                }
                div(className = "pf-c-toolbar__item pf-m-pagination") {
                    pagination(state)
                }
            }
        }
    }
}

fun Container.bulkSelect(state: State) {
    lateinit var menu: Ul
    div(className = "pf-c-dropdown") dropdown@{
        div(className = "pf-c-dropdown__toggle pf-m-split-button") {
            label(forId = "toolbar-selection-state", className = "pf-c-dropdown__toggle-check") {
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
                span(state.selectionState.info, className = "pf-c-dropdown__toggle-text") {
                    id = "toolbar-selection-state-info"
                    setAttribute("aria-hidden", "true")
                }
            }
            button("", icon = "fas fa-caret-down", className = "pf-c-dropdown__toggle-button") {
                id = "toolbar-selection-state-button"
                setAttribute("aria-haspopup", "true")
                setAttribute("aria-expanded", "false")
                onClick {
                    val isExpanded = this.getAttribute("aria-expanded") == "true"
                    if (isExpanded) {
                        this.setAttribute("aria-expanded", "false")
                        this@dropdown.removeCssClass("pf-m-expanded")
                        menu.setAttribute("hidden", "hidden")
                    } else {
                        this.setAttribute("aria-expanded", "true")
                        this@dropdown.addCssClass("pf-m-expanded")
                        menu.removeAttribute("hidden")
                    }
                }
            }
        }
        menu = ul(className = "pf-c-dropdown__menu") {
            role = "menu"
            setAttribute("aria-labelledby", "toolbar-selection-state-button")
            setAttribute("hidden", "hidden")
            li {
                role = "menuitem"
                button("Select none", className = "pf-c-dropdown__menu-item").onClick {
                    Model.selectNone()
                }
            }
            li {
                role = "menuitem"
                button("Select visible", className = "pf-c-dropdown__menu-item").onClick {
                    Model.selectVisible()
                }
            }
            li {
                role = "menuitem"
                button("Select all", className = "pf-c-dropdown__menu-item").onClick {
                    Model.selectAll()
                }
            }
        }
    }
}

fun Container.searchBox(state: State) {
    div(className = "pf-c-input-group") {
        val input = textInput(TextInputType.SEARCH, state.search, className = "pf-c-form-control") {
            setAttribute("aria-invalid", "false")
        }
        button("", "fas fa-search", className = "pf-c-button pf-m-control").onClick {
            Model.search(input.value)
        }
    }
}

fun Container.sortSelect(state: State) {
    lateinit var menu: Div
    div(className = "pf-c-options-menu") menu@{
        button("", icon = "fas fa-sort-amount-down", className = "pf-c-options-menu__toggle pf-m-plain") {
            id = "toolbar-sorting-button"
            setAttribute("aria-haspopup", "listbox")
            setAttribute("aria-expanded", "false")
            onClick {
                val isExpanded = this.getAttribute("aria-expanded") == "true"
                if (isExpanded) {
                    this.setAttribute("aria-expanded", "false")
                    this@menu.removeCssClass("pf-m-expanded")
                    menu.setAttribute("hidden", "hidden")
                } else {
                    this.setAttribute("aria-expanded", "true")
                    this@menu.addCssClass("pf-m-expanded")
                    menu.removeAttribute("hidden")
                }
            }
        }
        menu = div(className = "pf-c-options-menu__menu") {
            role = "menu"
            setAttribute("aria-labelledby", "toolbar-sorting-button")
            setAttribute("hidden", "hidden")
            section(className = "pf-c-options-menu__group") {
                ul {
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
                }
            }
            div(className = "pf-c-divider") {
                role = "separator"
            }
            section(className = "pf-c-options-menu__group") {
                ul {
                    menuItem("Ascending", state.sortType == SortType.ASC) {
                        Model.setSortType(SortType.ASC)
                    }
                    menuItem("Descending", state.sortType == SortType.DESC) {
                        Model.setSortType(SortType.DESC)
                    }
                }
            }
        }
    }
}

fun Container.pagination(state: State) {
    lateinit var menu: Ul
    val itemsFiltered = state.usersFiltered()
    val rowsInfo = "${(state.page - 1) * state.perPage + 1} - ${min(itemsFiltered.size, state.page * state.perPage)}"
    val lastPage = ((itemsFiltered.size - 1) / state.perPage) + 1
    div(className = "pf-c-pagination pf-m-compact") {
        div(className = "pf-c-pagination__total-items") {
            b(rowsInfo)
            +" of "
            b("${itemsFiltered.size}")
        }
        div(className = "pf-c-options-menu") menu@{
            div(className = "pf-c-options-menu__toggle pf-m-text pf-m-plain") {
                span(className = "pf-c-options-menu__toggle-text") {
                    b(rowsInfo)
                    +" of "
                    b("${itemsFiltered.size}")
                }
                button("", className = "pf-c-options-menu__toggle-button") {
                    id = "toolbar-pagination-menu-button"
                    setAttribute("aria-haspopup", "listbox")
                    setAttribute("aria-expanded", "false")
                    span(className = "pf-c-options-menu__toggle-button-icon") {
                        i(className = "fas fa-caret-down")
                    }
                    onClick {
                        val isExpanded = this.getAttribute("aria-expanded") == "true"
                        if (isExpanded) {
                            this.setAttribute("aria-expanded", "false")
                            this@menu.removeCssClass("pf-m-expanded")
                            menu.setAttribute("hidden", "hidden")
                        } else {
                            this.setAttribute("aria-expanded", "true")
                            this@menu.addCssClass("pf-m-expanded")
                            menu.removeAttribute("hidden")
                        }
                    }
                }
            }
            menu = ul(className = "pf-c-options-menu__menu") {
                role = "menu"
                setAttribute("aria-labelledby", "toolbar-pagination-menu-button")
                setAttribute("hidden", "hidden")
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
        }
        nav(className = "pf-c-pagination__nav") {
            div(className = "pf-c-pagination__nav-control pf-m-prev") {
                button("", "fas fa-angle-left", className = "pf-c-button pf-m-plain") {
                    disabled = state.page == 1
                    setAttribute("aria-label", "Go to previous page")
                    onClick {
                        Model.prevPage()
                    }
                }
            }
            div(className = "pf-c-pagination__nav-control pf-m-next") {
                button("", "fas fa-angle-right", className = "pf-c-button pf-m-plain") {
                    disabled = state.page == lastPage
                    setAttribute("aria-label", "Go to next page")
                    onClick {
                        Model.nextPage()
                    }
                }
            }
        }
    }
}
