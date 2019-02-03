package com.example

import pl.treksoft.kvision.remote.KVRemoteAgent

object PingAgent : KVRemoteAgent<PingService>(PingServiceManager)

actual class PingService : IPingService {
    override suspend fun ping(message: String) = PingAgent.call(IPingService::ping, message)
}
