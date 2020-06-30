package com.example

import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.tabbar.Tab

fun Tab.cameraPage() {
    page(className = "cameraPage") {
        div(className = "camera") {
            div(className = "focus")
        }
    }
}
