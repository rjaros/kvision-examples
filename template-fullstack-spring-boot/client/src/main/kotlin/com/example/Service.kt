package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.SpringRemoteAgent

object PingAgent : SpringRemoteAgent<PingService>(PingServiceManager)

actual class PingService actual constructor() {
    actual fun ping(message: String): Deferred<String> =
        PingAgent.call(PingService::ping, message)
}
