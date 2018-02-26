package com.example

import pl.treksoft.kvision.form.check.CheckBox.Companion.checkBox
import pl.treksoft.kvision.form.check.CheckBoxStyle
import pl.treksoft.kvision.form.check.Radio.Companion.radio
import pl.treksoft.kvision.form.check.RadioStyle
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.panel.FlexWrap
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.utils.px

class ButtonsTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        hPanel(wrap = FlexWrap.WRAP, spacing = 100) {
            vPanel(spacing = 7) {
                button("Default button", style = ButtonStyle.DEFAULT) { width = 200.px }
                button("Primary button", style = ButtonStyle.PRIMARY) { width = 200.px }
                button("Success button", style = ButtonStyle.SUCCESS) { width = 200.px }
                button("Info button", style = ButtonStyle.INFO) { width = 200.px }
                button("Warning button", style = ButtonStyle.WARNING) { width = 200.px }
                button("Danger button", style = ButtonStyle.DANGER) { width = 200.px }
                button("Link button", style = ButtonStyle.LINK) { width = 200.px }
            }
            vPanel {
                checkBox(true, label = "Default checkbox") { style = CheckBoxStyle.DEFAULT }
                checkBox(true, label = "Primary checkbox") { style = CheckBoxStyle.PRIMARY }
                checkBox(true, label = "Success checkbox") { style = CheckBoxStyle.SUCCESS }
                checkBox(true, label = "Info checkbox") { style = CheckBoxStyle.INFO }
                checkBox(true, label = "Warning checkbox") { style = CheckBoxStyle.WARNING }
                checkBox(true, label = "Danger checkbox") { style = CheckBoxStyle.DANGER }
                checkBox(true, label = "Circled checkbox") { circled = true }
            }
            vPanel {
                radio(name = "radio", label = "Default radiobutton") { style = RadioStyle.DEFAULT }
                radio(name = "radio", label = "Primary radiobutton") { style = RadioStyle.PRIMARY }
                radio(name = "radio", label = "Success radiobutton") { style = RadioStyle.SUCCESS }
                radio(name = "radio", label = "Info radiobutton") { style = RadioStyle.INFO }
                radio(name = "radio", label = "Warning radiobutton") { style = RadioStyle.WARNING }
                radio(name = "radio", label = "Danger radiobutton") { style = RadioStyle.DANGER }
                radio(name = "radio", label = "Squared radiobutton") { squared = true }
            }
        }
    }
}
