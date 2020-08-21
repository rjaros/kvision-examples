package com.example

import pl.treksoft.kvision.form.check.CheckBoxStyle
import pl.treksoft.kvision.form.check.RadioStyle
import pl.treksoft.kvision.form.check.checkBox
import pl.treksoft.kvision.form.check.radio
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.span
import pl.treksoft.kvision.i18n.I18n.gettext
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.core.FlexWrap
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.react.react
import pl.treksoft.kvision.require
import pl.treksoft.kvision.toolbar.buttonGroup
import pl.treksoft.kvision.toolbar.toolbar
import pl.treksoft.kvision.utils.px
import react.RClass
import react.RProps
import kotlinx.browser.window

external interface ReactButtonProps : RProps {
    var type: String
    var size: String
    var action: (dynamic, () -> Unit) -> Unit
}

@Suppress("UnsafeCastFromDynamic")
val ReactButton: RClass<ReactButtonProps> = require("react-awesome-button").AwesomeButtonProgress

class ButtonsTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        hPanel(wrap = FlexWrap.WRAP, spacing = 100) {
            vPanel(spacing = 7) {
                button(tr("Primary button"), style = ButtonStyle.PRIMARY) { width = 250.px }
                button(tr("Success button"), style = ButtonStyle.SUCCESS) { width = 250.px }
                button(tr("Info button"), style = ButtonStyle.INFO) { width = 250.px }
                button(tr("Warning button"), style = ButtonStyle.WARNING) { width = 250.px }
                button(tr("Danger button"), style = ButtonStyle.DANGER) { width = 250.px }
                button(tr("Link button"), style = ButtonStyle.LINK) { width = 250.px }
            }
            vPanel {
                checkBox(true, label = tr("Default checkbox"))
                checkBox(true, label = tr("Primary checkbox")) { style = CheckBoxStyle.PRIMARY }
                checkBox(true, label = tr("Success checkbox")) { style = CheckBoxStyle.SUCCESS }
                checkBox(true, label = tr("Info checkbox")) { style = CheckBoxStyle.INFO }
                checkBox(true, label = tr("Warning checkbox")) { style = CheckBoxStyle.WARNING }
                checkBox(true, label = tr("Danger checkbox")) { style = CheckBoxStyle.DANGER }
                checkBox(true, label = tr("Circled checkbox")) { circled = true }
            }
            vPanel {
                radio(name = "radio", label = tr("Default radiobutton"))
                radio(name = "radio", label = tr("Primary radiobutton")) { style = RadioStyle.PRIMARY }
                radio(name = "radio", label = tr("Success radiobutton")) { style = RadioStyle.SUCCESS }
                radio(name = "radio", label = tr("Info radiobutton")) { style = RadioStyle.INFO }
                radio(name = "radio", label = tr("Warning radiobutton")) { style = RadioStyle.WARNING }
                radio(name = "radio", label = tr("Danger radiobutton")) { style = RadioStyle.DANGER }
                radio(name = "radio", label = tr("Squared radiobutton")) { squared = true }
            }
        }
        hPanel(wrap = FlexWrap.WRAP, spacing = 100) {
            toolbar {
                buttonGroup {
                    button("<<")
                }
                buttonGroup {
                    button("1", disabled = true)
                    button("2")
                    button("3")
                }
                buttonGroup {
                    span("...")
                }
                buttonGroup {
                    button("10")
                }
                buttonGroup {
                    button(">>")
                }
            }
            react {
                ReactButton {
                    attrs.type = "primary"
                    attrs.size = "large"
                    attrs.action = { _, next ->
                        window.setTimeout({
                            next()
                        }, 3000)
                    }
                    +gettext("React progress button")
                }
            }
        }
    }
}
