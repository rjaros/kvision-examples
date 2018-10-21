package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.JoobyServiceManager
import pl.treksoft.kvision.remote.Request

expect class PingService() {
    fun ping(message: String, req: Request? = null): Deferred<String>
}

object PingServiceManager : JoobyServiceManager<PingService>(PingService()) {
    init {
        bind(PingService::ping)
    }
}
