package com.example

import com.example.service.EncodingServiceManager
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.slf4j.event.Level
import pl.treksoft.kvision.remote.applyRoutes
import pl.treksoft.kvision.remote.kvisionInit

@ExperimentalCoroutinesApi
@KtorExperimentalAPI
fun Application.main() {
    kvisionInit()
    install(CallLogging) {
        level = Level.DEBUG
    }
    routing {
        applyRoutes(EncodingServiceManager)
    }
}