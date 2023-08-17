package com.example

import io.kvision.core.AlignItems
import io.kvision.core.Container
import io.kvision.core.JustifyItems
import io.kvision.core.getElementJQueryD
import io.kvision.core.onClickLaunch
import io.kvision.form.check.checkBoxInput
import io.kvision.html.article
import io.kvision.html.div
import io.kvision.html.i
import io.kvision.html.span
import io.kvision.panel.gridPanel
import io.kvision.panel.hPanel
import io.kvision.state.bind
import io.kvision.state.bindEach
import io.kvision.toast.Toast
import io.kvision.utils.em
import io.kvision.utils.px
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

fun Container.cardView(stateFlow: StateFlow<State>, actionFlow: MutableSharedFlow<Action>) {
    gridPanel(
        templateColumns = "repeat(auto-fill, minmax(250px, 1fr))",
        justifyItems = JustifyItems.CENTER,
        columnGap = 20,
        className = "ui cards"
    ).bindEach(stateFlow, { it.usersVisible() }) { user ->
        card(stateFlow, user, actionFlow)
    }
}

fun Container.card(stateFlow: StateFlow<State>, user: User, actionFlow: MutableSharedFlow<Action>) {
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
                            Toast.danger("Edit ${user.name} not yet implemented")
                        }
                        menuItem("Remove", false) {
                            Toast.danger("Remove ${user.name} not yet implemented")
                        }
                    }
                    addAfterInsertHook {
                        @Suppress("UnsafeCastFromDynamic")
                        getElementJQueryD().dropdown()
                    }
                }
                checkBoxInput().bind(stateFlow, { it.selected.contains(user.login.uuid) }) {
                    value = it
                }.onClickLaunch {
                    if (this.value) {
                        actionFlow.emit(Action.SelectUser(user.login.uuid))
                    } else {
                        actionFlow.emit(Action.DeSelectUser(user.login.uuid))
                    }
                }
            }
            hPanel(className = "description", spacing = 10, alignItems = AlignItems.CENTER) {
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
