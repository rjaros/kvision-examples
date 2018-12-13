package com.example

import pl.treksoft.kvision.remote.Request

actual class PingService : IPingService {

    override suspend fun ping(message: String, req: Request?): String {
        println(message)
        return "Hello world from server!"
    }
}
