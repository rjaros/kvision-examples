package io.ktor.samples.fullstack.mpp

import io.kvision.remote.KVRemoteAgent
import kotlinx.serialization.modules.SerializersModule
import org.w3c.fetch.RequestInit

actual class PingService(serializersModules: List<SerializersModule>? = null, requestFilter: (RequestInit.() -> Unit)? = null) : IPingService, KVRemoteAgent<PingService>(PingServiceManager, serializersModules, requestFilter) {
    override suspend fun ping(message: String) = call(IPingService::ping, message)
}
