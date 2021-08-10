package com.example

import io.kvision.core.onEvent
import io.kvision.html.Align
import io.kvision.html.p
import io.kvision.onsenui.core.Navigator
import io.kvision.onsenui.core.backButton
import io.kvision.onsenui.core.page
import io.kvision.onsenui.toolbar.toolbar

fun Navigator.presentationPage() {
    page("presentation") {
        val toolbar = toolbar("Dialogs") {
            left {
                backButton("Anim")
            }
        }
        p("Use the OnsBackButton", align = Align.CENTER)
        onEvent {
            init = {
                @Suppress("UnsafeCastFromDynamic")
                toolbar.centerPanel.content = this@presentationPage.topPage.data.title
            }
        }
    }
}
