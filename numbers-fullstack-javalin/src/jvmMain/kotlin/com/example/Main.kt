package com.example

import io.javalin.Javalin
import io.kvision.remote.applyRoutes
import io.kvision.remote.kvisionInit

fun main() {
    Javalin.create().start(8080).apply {
        kvisionInit()
        applyRoutes(NumberServiceManager)
    }
}
