package com.example

import io.kvision.html.button
import io.kvision.html.section
import io.kvision.onsenui.FloatDirection
import io.kvision.onsenui.FloatPosition
import io.kvision.onsenui.control.fab
import io.kvision.onsenui.control.segment
import io.kvision.onsenui.control.speedDial
import io.kvision.onsenui.control.speedDialItem
import io.kvision.onsenui.core.Navigator
import io.kvision.onsenui.core.backButton
import io.kvision.onsenui.core.page
import io.kvision.onsenui.dialog.showConfirm
import io.kvision.onsenui.form.OnsButtonType
import io.kvision.onsenui.form.onsButton
import io.kvision.onsenui.toolbar.toolbar
import io.kvision.utils.px

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
