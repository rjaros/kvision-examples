package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.SpringServiceManager

interface IPingService {
    fun ping(message: String): Deferred<String>
}

expect class PingService : IPingService

object PingServiceManager : SpringServiceManager<PingService>(PingService::class) {
    init {
        bind(IPingService::ping)
    }
}
