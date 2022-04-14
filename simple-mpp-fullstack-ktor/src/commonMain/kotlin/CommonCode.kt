package io.ktor.samples.fullstack.mpp

import io.kvision.remote.HttpMethod
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import io.kvision.remote.KVServiceManager

interface IPingService {
    suspend fun ping(message: String): String
}

expect class PingService : IPingService

object PingServiceManager : KVServiceManager<PingService>(PingService::class) {
    init {
        bind(PingService::ping, HttpMethod.POST, null)
    }
}
