package com.example

import pl.treksoft.kvision.remote.JoobyRemoteAgent
import pl.treksoft.kvision.remote.Request

object PingAgent : JoobyRemoteAgent<PingService>(PingServiceManager)

actual class PingService : IPingService {
    override suspend fun ping(message: String, req: Request?) = PingAgent.call(IPingService::ping, message)
}
