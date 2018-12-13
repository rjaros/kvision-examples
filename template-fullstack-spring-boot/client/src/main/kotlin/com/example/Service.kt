package com.example

import pl.treksoft.kvision.remote.SpringRemoteAgent

object PingAgent : SpringRemoteAgent<PingService>(PingServiceManager)

actual class PingService : IPingService {
    override suspend fun ping(message: String) = PingAgent.call(IPingService::ping, message)
}
