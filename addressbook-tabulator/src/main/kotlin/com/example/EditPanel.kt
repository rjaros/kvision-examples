package com.example

import pl.treksoft.kvision.form.FormPanel
import pl.treksoft.kvision.form.FormPanel.Companion.formPanel
import pl.treksoft.kvision.form.check.CheckBox
import pl.treksoft.kvision.form.text.Text
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.HPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.StackPanel
import pl.treksoft.kvision.utils.ENTER_KEY
import pl.treksoft.kvision.utils.px

object EditPanel : StackPanel() {

    private var editingId: Int? = null

    private val formPanel: FormPanel<Address>
    private val mainPanel: SimplePanel

    init {
        padding = 10.px

        formPanel = formPanel {
            add(Address::firstName, Text(label = tr("First name:")))
            add(Address::lastName, Text(label = tr("Last name:")))
            add(Address::email, Text(TextInputType.EMAIL, label = tr("E-mail:")))
            add(Address::favourite, CheckBox(label = tr("Mark as favourite")))

            add(HPanel(spacing = 10) {
                button(tr("Save"), "fa-check", ButtonStyle.PRIMARY).onClick {
                    save()
                }
                button(tr("Cancel"), "fa-times", ButtonStyle.DEFAULT).onClick {
                    cancel()
                }
            })
            setEventListener {
                keydown = { e ->
                    if (e.keyCode == ENTER_KEY) {
                        save()
                    }
                }
            }
        }
        mainPanel = simplePanel {
            button(tr("Add new address"), "fa-plus", style = ButtonStyle.PRIMARY) {
                onClick {
                    add()
                }
            }
        }
    }

    private fun add() {
        editingId = null
        formPanel.clearData()
        activeChild = formPanel
        formPanel.getControl(Address::firstName)?.focus()
    }

    fun edit(id: Int) {
        Model.findAddress(id)?.let {
            editingId = id
            formPanel.setData(it)
            activeChild = formPanel
            formPanel.getControl(Address::firstName)?.focus()
        }
    }

    private fun save() {
        editingId?.let {
            Model.saveAddress(it, formPanel.getData())
        } ?: Model.addAddress(formPanel.getData())
        editingId = null
        activeChild = mainPanel
    }

    private fun cancel() {
        editingId = null
        activeChild = mainPanel
    }

    fun delete(id: Int) {
        Model.delAddress(id)
        if (id == editingId) {
            cancel()
        }
    }
}
