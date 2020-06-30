package com.example

import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.html.Align
import pl.treksoft.kvision.html.p
import pl.treksoft.kvision.onsenui.core.Navigator
import pl.treksoft.kvision.onsenui.core.backButton
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.toolbar.toolbar

fun Navigator.presentationPage() {
    page("presentation") {
        val toolbar = toolbar("Dialogs") {
            left {
                backButton("Anim")
            }
        }
        p("Use the OnsBackButton", align = Align.CENTER)
        onEvent {
            onsInit = {
                @Suppress("UnsafeCastFromDynamic")
                toolbar.centerPanel.content = this@presentationPage.topPage.data.title
            }
        }
    }
}
