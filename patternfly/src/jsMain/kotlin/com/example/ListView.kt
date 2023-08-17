package com.example

import io.kvision.core.Container
import io.kvision.form.check.checkBoxInput
import io.kvision.html.*
import io.kvision.toast.Toast

fun Container.listView(state: State) {
    ul(className = "pf-c-data-list") {
        state.usersVisible().forEach {
            listRow(state, it)
        }
    }
}

fun Container.listRow(state: State, user: User) {
    lateinit var section: Section
    li(className = "pf-c-data-list__item") li@{
        setAttribute("aria-labelledby", user.login.uuid)
        div(className = "pf-c-data-list__item-row") {
            div(className = "pf-c-data-list__item-control") {
                div(className = "pf-c-data-list__toggle") {
                    button("", className = "pf-c-button pf-m-plain") {
                        setAttribute("aria-label", "Details")
                        setAttribute("aria-expanded", "false")
                        div(className = "pf-c-data-list__toggle-icon") {
                            i(className = "fas fa-angle-right")
                        }
                        onClick {
                            if (this.getAttribute("aria-expanded") == "true") {
                                this.setAttribute("aria-expanded", "false")
                                this@li.removeCssClass("pf-m-expanded")
                                section.setAttribute("hidden", "hidden")
                            } else {
                                this.setAttribute("aria-expanded", "true")
                                this@li.addCssClass("pf-m-expanded")
                                section.removeAttribute("hidden")
                            }
                        }
                    }
                }
                div(className = "pf-c-data-list__check") {
                    checkBoxInput(state.selected.contains(user.login.uuid)) {
                        setAttribute("aria-labelledby", user.login.uuid)
                        onClick {
                            Model.selectUser(this.value, user.login.uuid)
                        }
                    }
                }
            }
            div(className = "pf-c-data-list__item-content") {
                div(className = "pf-c-data-list__cell pf-m-icon") {
                    natImage(user)
                }
                div(className = "pf-c-data-list__cell") {
                    p(user.name.toString()) {
                        id = user.login.uuid
                    }
                }
                div(className = "pf-c-data-list__cell") {
                    p {
                        icon("fas fa-user-alt pf-u-mr-sm")
                        +user.login.username
                    }
                    p {
                        small {
                            +"MD5: "
                            code(user.login.md5)
                        }
                    }
                    p {
                        small {
                            +"SHA-1: "
                            code(user.login.sha1)
                        }
                    }
                }
            }
            div(className = "pf-c-data-list__item-action") {
                button("Edit", className = "pf-c-button pf-m-secondary").onClick {
                    Toast.danger("Edit ${user.name} not yet implemented")
                }
                button("Remove", className = "pf-c-button pf-m-secondary").onClick {
                    Toast.danger("Remove ${user.name} not yet implemented")
                }
            }
        }
        section = section(className = "pf-c-data-list__expandable-content") {
            setAttribute("hidden", "hidden")
            div(className = "pf-c-data-list__expandable-content-body") {
                div(className = "pf-l-flex pf-m-align-items-center pf-m-space-items-2xl") {
                    userInfo(user)
                }
            }
        }
    }
}
