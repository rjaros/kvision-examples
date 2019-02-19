package io.ktor.samples.fullstack.mpp

import pl.treksoft.kvision.remote.KVRemoteAgent

actual class PingService : IPingService, KVRemoteAgent<PingService>(PingServiceManager) {
    override suspend fun ping(message: String) = call(IPingService::ping, message)
}
