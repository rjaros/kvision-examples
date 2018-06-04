package com.example

import kotlinx.coroutines.experimental.Deferred
import pl.treksoft.kvision.remote.RemoteAgent
import pl.treksoft.kvision.remote.Request

object PingAgent : RemoteAgent<PingService>(PingServiceManager)

actual class PingService actual constructor() {
    actual fun ping(message: String, req: Request?): Deferred<String> =
        PingAgent.call(PingService::ping, message)
}
