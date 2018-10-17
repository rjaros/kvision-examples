package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.Request
import pl.treksoft.kvision.remote.ServiceManager

expect class PingService() {
    fun ping(message: String, req: Request? = null): Deferred<String>
}

object PingServiceManager : ServiceManager<PingService>(PingService()) {
    init {
        bind(PingService::ping)
    }
}
