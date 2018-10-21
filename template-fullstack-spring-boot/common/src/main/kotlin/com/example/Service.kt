package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.SpringServiceManager

expect class PingService() {
    fun ping(message: String): Deferred<String>
}

object PingServiceManager : SpringServiceManager<PingService>(PingService::class) {
    init {
        bind(PingService::ping)
    }
}
