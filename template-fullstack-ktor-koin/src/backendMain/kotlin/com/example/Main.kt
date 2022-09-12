package com.example

import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.routing.*
import io.kvision.remote.applyRoutes
import io.kvision.remote.getServiceManager
import io.kvision.remote.kvisionInit
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun Application.main() {
    install(Compression)
    routing {
        applyRoutes(getServiceManager<IPingService>())
    }
    val module = module {
        factoryOf(::PingService)
    }
    kvisionInit(module)
}
