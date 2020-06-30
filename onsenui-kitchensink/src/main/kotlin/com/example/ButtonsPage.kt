package com.example

import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.section
import pl.treksoft.kvision.onsenui.FloatDirection
import pl.treksoft.kvision.onsenui.FloatPosition
import pl.treksoft.kvision.onsenui.control.fab
import pl.treksoft.kvision.onsenui.control.segment
import pl.treksoft.kvision.onsenui.control.speedDial
import pl.treksoft.kvision.onsenui.control.speedDialItem
import pl.treksoft.kvision.onsenui.core.Navigator
import pl.treksoft.kvision.onsenui.core.backButton
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.dialog.showConfirm
import pl.treksoft.kvision.onsenui.form.OnsButtonType
import pl.treksoft.kvision.onsenui.form.onsButton
import pl.treksoft.kvision.onsenui.toolbar.toolbar
import pl.treksoft.kvision.utils.px

fun Navigator.buttonsPage() {
    page("buttons") {
        toolbar("Buttons") {
            left {
                backButton("Home")
            }
        }
        section(rich = true) {
            margin = 16.px
            segment {
                width = 260.px
                button("First")
                button("Second")
                button("Third")
            }
            +"<br><br>"
            onsButton("Normal", className = "button-margin")
            onsButton("Quiet", buttonType = OnsButtonType.QUIET, className = "button-margin")
            onsButton("Outline", buttonType = OnsButtonType.OUTLINE, className = "button-margin")
            onsButton("Call to action", buttonType = OnsButtonType.CTA, className = "button-margin")
            onsButton("Light", buttonType = OnsButtonType.LIGHT, className = "button-margin")
            onsButton("Large", large = true, className = "button-margin")
        }
        section(rich = true) {
            margin = 16.px
            segment {
                disabled = true
                width = 260.px
                button("First")
                button("Second")
                button("Third")
            }
            +"<br><br>"
            onsButton("Normal", disabled = true, className = "button-margin")
            onsButton("Quiet", buttonType = OnsButtonType.QUIET, disabled = true, className = "button-margin")
            onsButton("Outline", buttonType = OnsButtonType.OUTLINE, disabled = true, className = "button-margin")
            onsButton("Call to action", buttonType = OnsButtonType.CTA, disabled = true, className = "button-margin")
            onsButton("Light", buttonType = OnsButtonType.LIGHT, disabled = true, className = "button-margin")
            onsButton("Large", large = true, disabled = true, className = "button-margin")
        }
        fixed {
            fab("md-face", FloatPosition.TOP_RIGHT)
            fab("md-car", FloatPosition.BOTTOM_LEFT)

            speedDial("md-share", FloatPosition.BOTTOM_RIGHT, FloatDirection.UP) {
                listOf(
                    "Twitter" to "md-twitter",
                    "Facebook" to "md-facebook",
                    "Google+" to "md-google-plus"
                ).forEach { (name, icon) ->
                    speedDialItem(icon).onClick {
                        showConfirm("Share on $name?")
                    }
                }
            }
        }
    }
}
