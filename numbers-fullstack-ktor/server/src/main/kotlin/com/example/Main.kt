package com.example

import io.ktor.application.Application
import io.ktor.routing.routing
import pl.treksoft.kvision.remote.applyRoutes
import pl.treksoft.kvision.remote.kvisionInit

fun Application.main() {
    kvisionInit()

    routing {
        applyRoutes(NumberServiceManager)
    }
}
