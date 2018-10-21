package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.JoobyRemoteAgent
import pl.treksoft.kvision.remote.Request

object PingAgent : JoobyRemoteAgent<PingService>(PingServiceManager)

actual class PingService actual constructor() {
    actual fun ping(message: String, req: Request?): Deferred<String> =
        PingAgent.call(PingService::ping, message)
}
