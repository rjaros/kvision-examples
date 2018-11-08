package com.example

import pl.treksoft.kvision.remote.Request
import pl.treksoft.kvision.remote.async

actual class PingService : IPingService {

    override fun ping(message: String, req: Request?) = req.async {
        println(message)
        "Hello world from server!"
    }
}
