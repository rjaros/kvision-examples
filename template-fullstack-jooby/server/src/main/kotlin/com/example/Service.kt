package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.Request
import pl.treksoft.kvision.remote.async

actual class PingService actual constructor() {

    actual fun ping(message: String, req: Request?): Deferred<String> = req.async {
        println(message)
        "Hello world from server!"
    }
}
