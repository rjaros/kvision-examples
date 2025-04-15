package com.example

import dev.kilua.rpc.applyRoutes
import dev.kilua.rpc.getServiceManager
import dev.kilua.rpc.initRpc
import dev.kilua.rpc.registerService
import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.routing.*
import io.kvision.remote.registerRemoteTypes

fun Application.main() {
    registerRemoteTypes()
    install(Compression)
    routing {
        applyRoutes(getServiceManager<IEncodingService>())
    }
    initRpc {
        registerService<IEncodingService> { EncodingService() }
    }
}
