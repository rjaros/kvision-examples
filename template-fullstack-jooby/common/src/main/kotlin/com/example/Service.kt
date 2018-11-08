package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.JoobyServiceManager
import pl.treksoft.kvision.remote.Request

interface IPingService {
    fun ping(message: String, req: Request? = null): Deferred<String>
}

expect class PingService() : IPingService

object PingServiceManager : JoobyServiceManager<PingService>(PingService()) {
    init {
        bind(PingService::ping)
    }
}
