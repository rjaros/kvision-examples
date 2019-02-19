package io.ktor.samples.fullstack.mpp

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.remote.KVServiceManager

interface IPingService {
    suspend fun ping(message: String): String
}

expect class PingService : IPingService

object PingServiceManager : KVServiceManager<PingService>(PingService::class) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(PingService::ping)
        }
    }
}
