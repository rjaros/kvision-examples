package com.example

import pl.treksoft.kvision.form.check.CHECKBOXSTYLE
import pl.treksoft.kvision.form.check.CheckBox.Companion.checkBox
import pl.treksoft.kvision.form.check.RADIOSTYLE
import pl.treksoft.kvision.form.check.Radio.Companion.radio
import pl.treksoft.kvision.html.BUTTONSTYLE
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.panel.FLEXWRAP
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.utils.px

class ButtonsTab : SimplePanel() {
    init {
        this.marginTop = 10.px()
        hPanel(wrap = FLEXWRAP.WRAP, spacing = 100) {
            vPanel(spacing = 7) {
                button("Default button", style = BUTTONSTYLE.DEFAULT) { width = 200.px() }
                button("Primary button", style = BUTTONSTYLE.PRIMARY) { width = 200.px() }
                button("Success button", style = BUTTONSTYLE.SUCCESS) { width = 200.px() }
                button("Info button", style = BUTTONSTYLE.INFO) { width = 200.px() }
                button("Warning button", style = BUTTONSTYLE.WARNING) { width = 200.px() }
                button("Danger button", style = BUTTONSTYLE.DANGER) { width = 200.px() }
                button("Link button", style = BUTTONSTYLE.LINK) { width = 200.px() }
            }
            vPanel {
                checkBox(true, label = "Default checkbox") { style = CHECKBOXSTYLE.DEFAULT }
                checkBox(true, label = "Primary checkbox") { style = CHECKBOXSTYLE.PRIMARY }
                checkBox(true, label = "Success checkbox") { style = CHECKBOXSTYLE.SUCCESS }
                checkBox(true, label = "Info checkbox") { style = CHECKBOXSTYLE.INFO }
                checkBox(true, label = "Warning checkbox") { style = CHECKBOXSTYLE.WARNING }
                checkBox(true, label = "Danger checkbox") { style = CHECKBOXSTYLE.DANGER }
                checkBox(true, label = "Circled checkbox") { circled = true }
            }
            vPanel {
                radio(name = "radio", label = "Default radiobutton") { style = RADIOSTYLE.DEFAULT }
                radio(name = "radio", label = "Primary radiobutton") { style = RADIOSTYLE.PRIMARY }
                radio(name = "radio", label = "Success radiobutton") { style = RADIOSTYLE.SUCCESS }
                radio(name = "radio", label = "Info radiobutton") { style = RADIOSTYLE.INFO }
                radio(name = "radio", label = "Warning radiobutton") { style = RADIOSTYLE.WARNING }
                radio(name = "radio", label = "Danger radiobutton") { style = RADIOSTYLE.DANGER }
                radio(name = "radio", label = "Squared radiobutton") { squared = true }
            }
        }
    }
}
