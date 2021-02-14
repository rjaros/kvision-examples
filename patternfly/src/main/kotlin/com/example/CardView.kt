package com.example

import io.kvision.core.Container
import io.kvision.form.check.checkBoxInput
import io.kvision.html.Ul
import io.kvision.html.article
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.icon
import io.kvision.html.span
import io.kvision.html.ul
import io.kvision.toast.Toast

fun Container.cardView(state: State) {
    div(className = "pf-l-gallery pf-m-gutter") {
        state.usersVisible().forEach {
            card(state, it)
        }
    }
}

fun Container.card(state: State, user: User) {
    article(className = "pf-c-card pf-m-hoverable pf-m-compact pf-m-flat sc-user-card") {
        div(className = "pf-c-card__header") {
            natImage(user)
            span(user.name.toString(), className = "pf-u-ml-sm sc-user-card__title")
            div(className = "pf-c-card__actions") {
                div(className = "pf-c-dropdown pf-m-align-right") dropdown@{
                    lateinit var menu: Ul
                    val menuButton =
                        button("", icon = "fas fa-ellipsis-v", className = "pf-c-dropdown__toggle pf-m-plain") {
                            id = "card-actions-button-${user.login.uuid}"
                            setAttribute("aria-haspopup", "true")
                            setAttribute("aria-expanded", "false")
                            onClick {
                                if (this.getAttribute("aria-expanded") == "true") {
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
                            Toast.error("Edit ${user.name} not yet implemented")
                            menuButton.setAttribute("aria-expanded", "false")
                            this@dropdown.removeCssClass("pf-m-expanded")
                            menu.setAttribute("hidden", "hidden")
                        }
                        menuItem("Remove", false, "pf-c-dropdown__menu-item") {
                            Toast.error("Remove ${user.name} not yet implemented")
                            menuButton.setAttribute("aria-expanded", "false")
                            this@dropdown.removeCssClass("pf-m-expanded")
                            menu.setAttribute("hidden", "hidden")
                        }
                    }
                }
                checkBoxInput(state.selected.contains(user.login.uuid)).onClick {
                    Model.selectUser(this.value, user.login.uuid)
                }
            }
        }
        div(className = "pf-c-card__body pf-l-flex pf-m-inline-flex pf-m-align-items-center") {
            photo(user)
            address(user)
        }
        div(className = "pf-c-card__footer") {
            icon("fas fa-user-alt pf-u-mr-sm")
            +user.login.username
        }
    }
}
