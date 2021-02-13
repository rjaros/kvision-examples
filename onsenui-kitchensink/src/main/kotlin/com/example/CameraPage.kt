package com.example

import io.kvision.html.div
import io.kvision.onsenui.core.page
import io.kvision.onsenui.tabbar.Tab

fun Tab.cameraPage() {
    page(className = "cameraPage") {
        div(className = "camera") {
            div(className = "focus")
        }
    }
}
