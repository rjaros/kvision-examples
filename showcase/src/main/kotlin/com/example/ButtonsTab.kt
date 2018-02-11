package com.example

import pl.treksoft.kvision.form.check.CHECKBOXSTYLE
import pl.treksoft.kvision.form.check.CheckBox
import pl.treksoft.kvision.form.check.RADIOSTYLE
import pl.treksoft.kvision.form.check.Radio
import pl.treksoft.kvision.html.BUTTONSTYLE
import pl.treksoft.kvision.html.Button
import pl.treksoft.kvision.panel.FLEXWRAP
import pl.treksoft.kvision.panel.HPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel
import pl.treksoft.kvision.utils.px

class ButtonsTab : SimplePanel() {
    init {
        this.marginTop = 10.px()
        this.add(HPanel(wrap = FLEXWRAP.WRAP, spacing = 100).apply {
            add(VPanel(spacing = 7).apply {
                add(Button("Default button", style = BUTTONSTYLE.DEFAULT).apply { width = 200.px() })
                add(Button("Primary button", style = BUTTONSTYLE.PRIMARY).apply { width = 200.px() })
                add(Button("Success button", style = BUTTONSTYLE.SUCCESS).apply { width = 200.px() })
                add(Button("Info button", style = BUTTONSTYLE.INFO).apply { width = 200.px() })
                add(Button("Warning button", style = BUTTONSTYLE.WARNING).apply { width = 200.px() })
                add(Button("Danger button", style = BUTTONSTYLE.DANGER).apply { width = 200.px() })
                add(Button("Link button", style = BUTTONSTYLE.LINK).apply { width = 200.px() })
            })
            add(VPanel().apply {
                add(CheckBox(true, label = "Default checkbox").apply { style = CHECKBOXSTYLE.DEFAULT })
                add(CheckBox(true, label = "Primary checkbox").apply { style = CHECKBOXSTYLE.PRIMARY })
                add(CheckBox(true, label = "Success checkbox").apply { style = CHECKBOXSTYLE.SUCCESS })
                add(CheckBox(true, label = "Info checkbox").apply { style = CHECKBOXSTYLE.INFO })
                add(CheckBox(true, label = "Warning checkbox").apply { style = CHECKBOXSTYLE.WARNING })
                add(CheckBox(true, label = "Danger checkbox").apply { style = CHECKBOXSTYLE.DANGER })
                add(CheckBox(true, label = "Circled checkbox").apply { circled = true })
            })
            add(VPanel().apply {
                add(Radio(name = "radio", label = "Default radiobutton").apply { style = RADIOSTYLE.DEFAULT })
                add(Radio(name = "radio", label = "Primary radiobutton").apply { style = RADIOSTYLE.PRIMARY })
                add(Radio(name = "radio", label = "Success radiobutton").apply { style = RADIOSTYLE.SUCCESS })
                add(Radio(name = "radio", label = "Info radiobutton").apply { style = RADIOSTYLE.INFO })
                add(Radio(name = "radio", label = "Warning radiobutton").apply { style = RADIOSTYLE.WARNING })
                add(Radio(name = "radio", label = "Danger radiobutton").apply { style = RADIOSTYLE.DANGER })
                add(Radio(name = "radio", label = "Squared radiobutton").apply { squared = true })
            })
        })
    }
}