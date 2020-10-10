package com.example

import kotlinx.browser.window
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.form.check.CheckBox
import pl.treksoft.kvision.form.formPanel
import pl.treksoft.kvision.form.text.Text
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.panel.simplePanel
import pl.treksoft.kvision.utils.ENTER_KEY
import pl.treksoft.kvision.utils.px

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
