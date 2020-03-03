package com.example

import io.javalin.Javalin
import pl.treksoft.kvision.remote.applyRoutes
import pl.treksoft.kvision.remote.kvisionInit

fun main() {
    Javalin.create().start(8080).apply {
        kvisionInit()
        applyRoutes(NumberServiceManager)
    }
}
