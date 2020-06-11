package com.example

import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.form.check.checkBox
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.form.text.textInput
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.html.Align
import pl.treksoft.kvision.html.Button
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.html.H4
import pl.treksoft.kvision.html.Image
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.i18n.gettext
import pl.treksoft.kvision.modal.Alert
import pl.treksoft.kvision.modal.Confirm
import pl.treksoft.kvision.modal.Modal
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.toast.Toast
import pl.treksoft.kvision.utils.px
import pl.treksoft.kvision.window.Window
import kotlin.random.Random

class ModalsTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px
        vPanel(spacing = 30) {
            button(tr("Alert dialog"), style = ButtonStyle.DANGER).onClick {
                Alert.show(
                    tr("Alert dialog"),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis, vel molestie dolor. Vestibulum ut ex eget orci porta gravida eu sit amet tortor."
                )
            }
            button(tr("Confirm dialog"), style = ButtonStyle.WARNING).onClick {
                Confirm.show(
                    tr("Confirm dialog"),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis, vel molestie dolor. Vestibulum ut ex eget orci porta gravida eu sit amet tortor.",
                    yesTitle = tr("Yes"),
                    noTitle = tr("No"),
                    cancelTitle = tr("Cancel"),
                    noCallback = {
                        Alert.show(tr("Result"), tr("You pressed NO button."))
                    }) {
                    Alert.show(tr("Result"), tr("You pressed YES button."))
                }
            }
            button(tr("Cancelable confirm dialog"), style = ButtonStyle.INFO).onClick {
                Confirm.show(
                    tr("Cancelable confirm dialog"),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis, vel molestie dolor. Vestibulum ut ex eget orci porta gravida eu sit amet tortor.",
                    align = Align.CENTER,
                    cancelVisible = true,
                    yesTitle = tr("Yes"),
                    noTitle = tr("No"),
                    cancelTitle = tr("Cancel"),
                    noCallback = {
                        Alert.show(tr("Result"), tr("You pressed NO button."))
                    }) {
                    Alert.show(tr("Result"), tr("You pressed YES button."))
                }
            }
            val modal = Modal(tr("Custom modal dialog"))
            modal.add(
                H4(
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis, vel molestie dolor. Vestibulum ut ex eget orci porta gravida eu sit amet tortor."
                )
            )
            modal.add(Image(require("img/dog.jpg")))
            modal.addButton(Button(tr("Close")).onClick {
                modal.hide()
            })
            button(tr("Custom modal dialog"), style = ButtonStyle.SUCCESS).onClick {
                modal.show()
            }
            button(tr("Alert dialog without animation"), style = ButtonStyle.PRIMARY).onClick {
                Alert.show(
                    tr("Alert dialog without animation"),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis, vel molestie dolor. Vestibulum ut ex eget orci porta gravida eu sit amet tortor.",
                    animation = false
                )
            }
            var counter = 1
            button(tr("Open new window"), style = ButtonStyle.PRIMARY, icon = "far fa-window-maximize").onClick {
                val sw = ShowcaseWindow(I18n.gettext("Window") + " " + counter++) {
                    left = ((Random.nextDouble() * 800).toInt()).px
                    top = ((Random.nextDouble() * 300).toInt()).px
                }
                this@ModalsTab.add(sw)
                sw.focus()
            }
            button(tr("Show toast message"), style = ButtonStyle.PRIMARY, icon = "fas fa-info-circle").onClick {
                Toast.info(gettext("This is a toast message"))
            }
        }
    }
}

class ShowcaseWindow(caption: String?, init: (ShowcaseWindow.() -> Unit)? = null) :
    Window(caption, 600.px, 300.px, closeButton = true) {

    lateinit var captionInput: TextInput

    init {
        init?.invoke(this)
        vPanel {
            margin = 10.px
            captionInput = textInput(TextInputType.TEXT, caption) {
                onEvent {
                    change = {
                        this@ShowcaseWindow.caption = self.value
                    }
                }
            }
            checkBox(true, label = tr("Draggable")).onClick {
                this@ShowcaseWindow.isDraggable = this.value
            }
            checkBox(true, label = tr("Resizable")).onClick {
                this@ShowcaseWindow.isResizable = this.value
            }
            checkBox(true, label = tr("Close button")).onClick {
                this@ShowcaseWindow.closeButton = this.value
            }
        }
    }

    override fun focus() {
        super.focus()
        captionInput.focus()
    }
}
