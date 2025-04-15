package com.example

import io.kvision.core.FlexWrap
import io.kvision.form.check.CheckStyle
import io.kvision.form.check.checkBox
import io.kvision.form.check.radio
import io.kvision.form.check.switch
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.span
import io.kvision.i18n.I18n.gettext
import io.kvision.i18n.I18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.gridPanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel
import io.kvision.react.react
import io.kvision.toolbar.buttonGroup
import io.kvision.toolbar.toolbar
import io.kvision.utils.px
import kotlinx.browser.window
import react.ComponentType
import react.PropsWithChildren

external interface ReactButtonProps : PropsWithChildren {
    var type: String
    var size: String
    var action: (dynamic, () -> Unit) -> Unit
}

@JsModule("react-awesome-button")
external val reactButtonModule: dynamic

@Suppress("UnsafeCastFromDynamic")
val ReactButton: ComponentType<ReactButtonProps> = reactButtonModule.AwesomeButtonProgress

class ButtonsTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        hPanel(wrap = FlexWrap.WRAP, spacing = 80) {
            marginBottom = 40.px
            vPanel(spacing = 7) {
                button(tr("Primary button"), style = ButtonStyle.PRIMARY) { width = 250.px }
                button(tr("Secondary button"), style = ButtonStyle.SECONDARY) { width = 250.px }
                button(tr("Success button"), style = ButtonStyle.SUCCESS) { width = 250.px }
                button(tr("Info button"), style = ButtonStyle.INFO) { width = 250.px }
                button(tr("Warning button"), style = ButtonStyle.WARNING) { width = 250.px }
                button(tr("Danger button"), style = ButtonStyle.DANGER) { width = 250.px }
                button(tr("Light button"), style = ButtonStyle.LIGHT) { width = 250.px }
                button(tr("Dark button"), style = ButtonStyle.DARK) { width = 250.px }
                button(tr("Link button"), style = ButtonStyle.LINK) { width = 250.px }
            }
            gridPanel(templateColumns = "auto auto", columnGap = 40, rowGap = 40) {
                vPanel {
                    checkBox(true, label = tr("Primary checkbox")) { style = CheckStyle.PRIMARY }
                    checkBox(true, label = tr("Secondary checkbox")) { style = CheckStyle.SECONDARY }
                    checkBox(true, label = tr("Success checkbox")) { style = CheckStyle.SUCCESS }
                    checkBox(true, label = tr("Info checkbox")) { style = CheckStyle.INFO }
                    checkBox(true, label = tr("Warning checkbox")) { style = CheckStyle.WARNING }
                    checkBox(true, label = tr("Danger checkbox")) { style = CheckStyle.DANGER }
                    checkBox(true, label = tr("Light checkbox")) { style = CheckStyle.LIGHT }
                    checkBox(true, label = tr("Dark checkbox")) { style = CheckStyle.DARK }
                    checkBox(true, label = tr("Circled checkbox")) { circled = true }
                }
                vPanel {
                    radio(true, name = "radio", label = tr("Primary radiobutton")) { style = CheckStyle.PRIMARY }
                    radio(name = "radio", label = tr("Secondary radiobutton")) { style = CheckStyle.SECONDARY }
                    radio(name = "radio", label = tr("Success radiobutton")) { style = CheckStyle.SUCCESS }
                    radio(name = "radio", label = tr("Info radiobutton")) { style = CheckStyle.INFO }
                    radio(name = "radio", label = tr("Warning radiobutton")) { style = CheckStyle.WARNING }
                    radio(name = "radio", label = tr("Danger radiobutton")) { style = CheckStyle.DANGER }
                    radio(name = "radio", label = tr("Light radiobutton")) { style = CheckStyle.LIGHT }
                    radio(name = "radio", label = tr("Dark radiobutton")) { style = CheckStyle.DARK }
                    radio(name = "radio", label = tr("Squared radiobutton")) { squared = true }
                }
                vPanel {
                    switch(true, label = tr("Primary switch")) { style = CheckStyle.PRIMARY }
                    switch(true, label = tr("Secondary switch")) { style = CheckStyle.SECONDARY }
                    switch(true, label = tr("Success switch")) { style = CheckStyle.SUCCESS }
                    switch(true, label = tr("Info switch")) { style = CheckStyle.INFO }
                    switch(true, label = tr("Warning switch")) { style = CheckStyle.WARNING }
                    switch(true, label = tr("Danger switch")) { style = CheckStyle.DANGER }
                    switch(true, label = tr("Light switch")) { style = CheckStyle.LIGHT }
                    switch(true, label = tr("Dark switch")) { style = CheckStyle.DARK }
                }
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
