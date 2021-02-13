package com.example

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.kvision.remote.applyRoutes
import io.kvision.remote.kvisionInit

fun Application.main() {
    install(Compression)
    routing {
        applyRoutes(PingServiceManager)
    }
    kvisionInit()
}
