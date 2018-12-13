package com.example

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.remote.SpringServiceManager

interface IPingService {
    suspend fun ping(message: String): String
}

expect class PingService : IPingService

object PingServiceManager : SpringServiceManager<PingService>(PingService::class) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(IPingService::ping)
        }
    }
}
