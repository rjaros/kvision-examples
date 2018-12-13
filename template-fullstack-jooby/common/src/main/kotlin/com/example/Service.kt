package com.example

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.remote.JoobyServiceManager
import pl.treksoft.kvision.remote.Request

interface IPingService {
    suspend fun ping(message: String, req: Request? = null): String
}

expect class PingService() : IPingService

object PingServiceManager : JoobyServiceManager<PingService>(PingService()) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(PingService::ping)
        }
    }
}
