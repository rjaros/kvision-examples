package com.example

import io.kvision.core.FlexWrap
import io.kvision.form.check.CheckBoxStyle
import io.kvision.form.check.RadioStyle
import io.kvision.form.check.checkBox
import io.kvision.form.check.radio
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.span
import io.kvision.i18n.I18n.gettext
import io.kvision.i18n.I18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel
import io.kvision.react.react
import io.kvision.require
import io.kvision.toolbar.buttonGroup
import io.kvision.toolbar.toolbar
import io.kvision.utils.px
import kotlinx.browser.window
import react.ChildrenBuilder
import react.ComponentClass
import react.Props
import react.PropsWithChildren

external interface ReactButtonProps : PropsWithChildren {
    var type: String
    var size: String
    var action: (dynamic, () -> Unit) -> Unit
}

@Suppress("UnsafeCastFromDynamic")
val ReactButton: ComponentClass<ReactButtonProps> = require("react-awesome-button").AwesomeButtonProgress

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
                    type = "primary"
                    size = "large"
                    action = { _, next ->
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
