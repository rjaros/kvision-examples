package com.example

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.Compression
import io.ktor.routing.routing
import pl.treksoft.kvision.remote.applyRoutes
import pl.treksoft.kvision.remote.kvisionInit

fun Application.main() {
    install(Compression)
    kvisionInit()

    routing {
        applyRoutes(PingServiceManager)
    }
}
