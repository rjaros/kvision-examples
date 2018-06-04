package com.example

import kotlinx.coroutines.experimental.Deferred
import pl.treksoft.kvision.remote.Request
import pl.treksoft.kvision.remote.async

actual class PingService actual constructor() {

    actual fun ping(message: String, req: Request?): Deferred<String> =
        req.async { _, _, _ ->
            println(message)
            "Hello world from server!"
        }
}
