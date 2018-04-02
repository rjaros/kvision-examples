package com.example

import pl.treksoft.kvision.html.Align
import pl.treksoft.kvision.html.Button
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.html.Image
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag
import pl.treksoft.kvision.modal.Alert
import pl.treksoft.kvision.modal.Confirm
import pl.treksoft.kvision.modal.Modal
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.px

class ModalsTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px
        vPanel(spacing = 30) {
            button("Alert dialog", style = ButtonStyle.DANGER).onClick {
                Alert.show(
                    "Alert dialog",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis, vel molestie dolor. Vestibulum ut ex eget orci porta gravida eu sit amet tortor."
                )
            }
            button("Confirm dialog", style = ButtonStyle.WARNING).onClick {
                Confirm.show(
                    "Confirm dialog",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis, vel molestie dolor. Vestibulum ut ex eget orci porta gravida eu sit amet tortor.",
                    noCallback = {
                        Alert.show("Result", "You pressed NO button.")
                    }) {
                    Alert.show("Result", "You pressed YES button.")
                }
            }
            button("Cancelable confirm dialog", style = ButtonStyle.INFO).onClick {
                Confirm.show(
                    "Cancelable confirm dialog",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis, vel molestie dolor. Vestibulum ut ex eget orci porta gravida eu sit amet tortor.",
                    align = Align.CENTER,
                    cancelVisible = true,
                    noCallback = {
                        Alert.show("Result", "You pressed NO button.")
                    }) {
                    Alert.show("Result", "You pressed YES button.")
                }
            }
            val modal = Modal("Custom modal dialog")
            modal.add(
                Tag(
                    TAG.H4,
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis, vel molestie dolor. Vestibulum ut ex eget orci porta gravida eu sit amet tortor."
                )
            )
            modal.add(Image(require("./img/dog.jpg")))
            modal.addButton(Button("Close").onClick {
                modal.hide()
            })
            button("Custom modal dialog", style = ButtonStyle.SUCCESS).onClick {
                modal.show()
            }
            button("Alert dialog without animation", style = ButtonStyle.PRIMARY).onClick {
                Alert.show(
                    "Alert dialog without animation",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis, vel molestie dolor. Vestibulum ut ex eget orci porta gravida eu sit amet tortor.",
                    animation = false
                )
            }
        }
    }
}
