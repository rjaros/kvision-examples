package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.form.FormPanel
import pl.treksoft.kvision.form.formPanel
import pl.treksoft.kvision.form.check.CheckBox
import pl.treksoft.kvision.form.text.Text
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.HPanel
import pl.treksoft.kvision.panel.StackPanel
import pl.treksoft.kvision.utils.ENTER_KEY
import pl.treksoft.kvision.utils.px

object EditPanel : StackPanel() {

    private var editingId: Int? = null

    private val formPanel: FormPanel<Address>

    init {
        padding = 10.px

        formPanel = formPanel {
            add(Address::firstName, Text(label = "${tr("First name")}:").apply { maxlength = 255 })
            add(Address::lastName, Text(label = "${tr("Last name")}:").apply { maxlength = 255 })
            add(Address::email, Text(TextInputType.EMAIL, label = "${tr("E-mail")}:").apply { maxlength = 255 }) {
                it.getValue()
                        ?.matches("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
            }
            add(Address::phone, Text(label = "${tr("Phone number")}:").apply { maxlength = 255 })
            add(Address::postalAddress, Text(label = "${tr("Postal address")}:").apply { maxlength = 255 })
            add(Address::favourite, CheckBox(label = tr("Mark as favourite")))

            add(HPanel(spacing = 10) {
                button(tr("Save"), "fas fa-check", ButtonStyle.PRIMARY).onClick {
                    save()
                }
                button(tr("Cancel"), "fas fa-times", ButtonStyle.SECONDARY).onClick {
                    close()
                }
            })
            onEvent {
                keydown = {
                    if (it.keyCode == ENTER_KEY) {
                        save()
                    }
                }
            }
        }
        add(MainPanel)
    }

    fun add() {
        formPanel.clearData()
        open(null)
    }

    fun edit(index: Int) {
        val address = Model.addresses[index]
        formPanel.setData(address)
        open(address.id)
    }

    private fun save() {
        GlobalScope.launch {
            if (formPanel.validate()) {
                val address = formPanel.getData()
                if (editingId != null) {
                    Model.updateAddress(address.copy(id = editingId))
                } else {
                    Model.addAddress(address)
                }
                close()
            }
        }
    }

    fun delete(index: Int) {
        GlobalScope.launch {
            close()
            Model.addresses[index].id?.let {
                Model.deleteAddress(it)
            }
        }
    }

    private fun open(editingId: Int?) {
        this.editingId = editingId
        activeChild = formPanel
        formPanel.validate()
        formPanel.getControl(Address::firstName)?.focus()
    }

    private fun close() {
        editingId = null
        activeChild = MainPanel
    }
}
