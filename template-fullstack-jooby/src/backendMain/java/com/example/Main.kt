package com.example

import io.jooby.runApp
import pl.treksoft.kvision.remote.applyRoutes
import pl.treksoft.kvision.remote.kvisionInit

fun main(args: Array<String>) {
    runApp(args) {
        kvisionInit()
        applyRoutes(PingServiceManager)
        onStarted {
            // Initialization
        }
    }
}
