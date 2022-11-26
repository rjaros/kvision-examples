package com.example

import io.kvision.core.Container
import io.kvision.form.check.checkBoxInput
import io.kvision.html.*
import io.kvision.toast.Toast
import kotlin.js.Date

fun Container.tableView(state: State) {
    table(className = "pf-c-table pf-m-grid-md") {
        role = "grid"
        thead {
            tr {
                role = "row"
                td()
                td(className = "pf-c-table__check") {
                    checkBoxInput {
                        setAttribute("aria-label", "Select all")
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
                            if (this.value) {
                                Model.selectAll()
                            } else {
                                Model.selectNone()
                            }
                        }
                    }
                }
                tabHeader("First name", SortItem.FIRST_NAME, state, "pf-m-with-25")
                tabHeader("Last name", SortItem.LAST_NAME, state, "pf-m-with-25")
                tabHeader("Birthday", SortItem.AGE, state)
                th("Registered") {
                    setAttribute("scope", "col")
                    role = "columnheader"
                }
                td()
            }
        }
        state.usersVisible().forEach {
            tableRow(state, it)
        }
    }
}

fun Tr.tabHeader(label: String, sortItem: SortItem, state: State, className: String? = null) {
    th(className = "pf-c-table__sort") {
        className?.let { addCssClass(it) }
        setAttribute("scope", "col")
        role = "columnheader"
        val (ariaSort, sortIcon) = if (state.sortItem == sortItem) {
            when (state.sortType) {
                SortType.ASC -> "ascending" to "fas fa-long-arrow-alt-up fas fa-arrows-alt-v"
                SortType.DESC -> "descending" to "fas fa-long-arrow-alt-down fas fa-arrows-alt-v"
            }
        } else {
            "none" to "fas fa-arrows-alt-v fas fa-arrows-alt-v"
        }
        setAttribute("aria-sort", ariaSort)
        button("", className = "pf-c-table__button") {
            span(label, className = "pf-c-table__text")
            span(className = "pf-c-table__sort-indicator") {
                i(className = sortIcon)
            }
            onClick {
                if (state.sortItem == sortItem) {
                    when (state.sortType) {
                        SortType.ASC -> Model.setSortType(SortType.DESC)
                        SortType.DESC -> Model.setSortType(SortType.ASC)
                    }
                } else {
                    Model.setSortItem(sortItem)
                    Model.setSortType(SortType.ASC)
                }
            }
        }
    }
}

fun Table.tableRow(state: State, user: User) {
    lateinit var tr: Tag
    tbody tbody@{
        tr {
            role = "row"
            td(className = "pf-c-table__toggle pf-l-flex pf-m-align-items-center pf-m-space-items-2xl") {
                role = "cell"
                button("", className = "pf-c-button pf-m-plain") {
                    setAttribute("aria-label", "Details")
                    setAttribute("aria-expanded", "false")
                    div(className = "pf-c-table__toggle-icon") {
                        i(className = "fas fa-angle-down")
                    }
                    onClick {
                        if (this.getAttribute("aria-expanded") == "true") {
                            this.setAttribute("aria-expanded", "false")
                            this.removeCssClass("pf-m-expanded")
                            this@tbody.removeCssClass("pf-m-expanded")
                            tr.setAttribute("hidden", "hidden")
                            tr.removeCssClass("pf-m-expanded")
                        } else {
                            this.setAttribute("aria-expanded", "true")
                            this.addCssClass("pf-m-expanded")
                            this@tbody.addCssClass("pf-m-expanded")
                            tr.removeAttribute("hidden")
                            tr.addCssClass("pf-m-expanded")
                        }
                    }
                }
            }
            td(className = "pf-c-table__check") {
                checkBoxInput(state.selected.contains(user.login.uuid)) {
                    setAttribute("aria-labelledby", user.login.uuid)
                    onClick {
                        Model.selectUser(this.value, user.login.uuid)
                    }
                }
            }
            td(user.name.first) {
                role = "cell"
                setAttribute("data-label", "First name")
            }
            td(user.name.last) {
                role = "cell"
                setAttribute("data-label", "Last name")
            }
            td(Date(Date.parse(user.dob.date)).toLocaleDateString("en")) {
                role = "cell"
                setAttribute("data-label", "Birthday")
            }
            td(Date(Date.parse(user.registered.date)).toLocaleDateString("en")) {
                role = "cell"
                setAttribute("data-label", "Registered")
            }
            td(className = "pf-c-table__action") {
                role = "cell"
                div(className = "pf-c-dropdown pf-m-align-right") dropdown@{
                    lateinit var menu: Ul
                    val menuButton =
                        button("", icon = "fas fa-ellipsis-v", className = "pf-c-dropdown__toggle pf-m-plain") {
                            id = "card-actions-button-${user.login.uuid}"
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
                    menu = ul(className = "pf-c-dropdown__menu pf-m-align-right") {
                        role = "menu"
                        setAttribute("aria-labelledby", "card-actions-button-${user.login.uuid}")
                        setAttribute("hidden", "hidden")
                        menuItem("Edit", false, "pf-c-dropdown__menu-item") {
                            Toast.danger("Edit ${user.name} not yet implemented")
                            menuButton.setAttribute("aria-expanded", "false")
                            this@dropdown.removeCssClass("pf-m-expanded")
                            menu.setAttribute("hidden", "hidden")
                        }
                        menuItem("Remove", false, "pf-c-dropdown__menu-item") {
                            Toast.danger("Remove ${user.name} not yet implemented")
                            menuButton.setAttribute("aria-expanded", "false")
                            this@dropdown.removeCssClass("pf-m-expanded")
                            menu.setAttribute("hidden", "hidden")
                        }
                    }
                }
            }
        }
        tr = tr(className = "pf-c-table__expandable-row") {
            role = "row"
            setAttribute("hidden", "hidden")
            td()
            td()
            td {
                setAttribute("colspan", "4")
                div(className = "pf-c-table__expandable-row-content pf-l-flex pf-m-align-items-center pf-m-space-items-2xl") {
                    userInfo(user)
                }
            }
            td()
        }
    }
}
