package com.example

import kotlinx.browser.window
import io.kvision.core.Container
import io.kvision.core.onEvent
import io.kvision.form.check.CheckBox
import io.kvision.form.formPanel
import io.kvision.form.text.Text
import io.kvision.form.text.TextInputType
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.i18n.I18n.tr
import io.kvision.panel.hPanel
import io.kvision.panel.simplePanel
import io.kvision.utils.ENTER_KEY
import io.kvision.utils.px

fun Container.editPanel() {
    simplePanel(Model.addressBook) { state ->
        padding = 10.px
        if (state.editMode != null) {
            val formPanel = formPanel<Address> {
                add(Address::firstName, Text(label = tr("First name:")))
                add(Address::lastName, Text(label = tr("Last name:")))
                add(Address::email, Text(TextInputType.EMAIL, label = tr("E-mail:")))
                add(Address::favourite, CheckBox(label = tr("Mark as favourite")))

                hPanel(spacing = 10) {
                    button(tr("Save"), "fas fa-check", ButtonStyle.PRIMARY).onClick {
                        Model.save(this@formPanel.getData())
                    }
                    button(tr("Cancel"), "fas fa-times", ButtonStyle.SECONDARY).onClick {
                        Model.cancel()
                    }
                }
                onEvent {
                    keydown = { e ->
                        if (e.keyCode == ENTER_KEY) {
                            Model.save(this@formPanel.getData())
                        }
                    }
                }
            }
            if (state.editMode == EditMode.NEW) {
                formPanel.clearData()
            } else if (state.editAddress != null) {
                formPanel.setData(state.editAddress)
            }
            window.setTimeout({
                formPanel.getControl(Address::firstName)?.focus()
            }, 0)
        } else {
            simplePanel {
                button(tr("Add new address"), "fas fa-plus", style = ButtonStyle.PRIMARY).onClick {
                    Model.add()
                }
            }
        }
    }
}
