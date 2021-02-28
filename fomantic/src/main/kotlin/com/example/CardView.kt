package com.example

import io.kvision.core.AlignItems
import io.kvision.core.Container
import io.kvision.core.JustifyItems
import io.kvision.form.check.checkBoxInput
import io.kvision.html.article
import io.kvision.html.div
import io.kvision.html.i
import io.kvision.html.span
import io.kvision.panel.gridPanel
import io.kvision.panel.hPanel
import io.kvision.redux.Dispatch
import io.kvision.toast.Toast
import io.kvision.utils.em
import io.kvision.utils.px

fun Container.cardView(state: State, dispatch: Dispatch<Action>) {
    gridPanel(
        templateColumns = "repeat(auto-fill, minmax(250px, 1fr))",
        justifyItems = JustifyItems.CENTER,
        noWrappers = true,
        columnGap = 20,
        className = "ui cards"
    ) {
        state.usersVisible().forEach {
            card(state, it, dispatch)
        }
    }
}

fun Container.card(state: State, user: User, dispatch: Dispatch<Action>) {
    article(className = "ui raised link centered card") {
        div(className = "content") {
            natImage(user)
            +" "
            span(user.name.toString())
            div(className = "ui right floated") {
                div(className = "ui basic floating dropdown icon compact mini button") {
                    i(className = "bars icon")
                    div(className = "menu") {
                        width = 150.px
                        menuItem("Edit", false) {
                            Toast.error("Edit ${user.name} not yet implemented")
                        }
                        menuItem("Remove", false) {
                            Toast.error("Remove ${user.name} not yet implemented")
                        }
                    }
                    addAfterInsertHook {
                        @Suppress("UnsafeCastFromDynamic")
                        getElementJQueryD().dropdown()
                    }
                }
                checkBoxInput(state.selected.contains(user.login.uuid)).onClick {
                    if (this.value) {
                        dispatch(Action.SelectUser(user.login.uuid))
                    } else {
                        dispatch(Action.DeSelectUser(user.login.uuid))
                    }
                }
            }
            hPanel(className = "description", spacing = 10, alignItems = AlignItems.CENTER, noWrappers = true) {
                marginTop = 1.em
                photo(user)
                address(user)
            }
        }
        div(className = "extra content") {
            i(className = "user icon")
            +user.login.username
        }
    }
}
