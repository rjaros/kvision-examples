package com.example

import io.micronaut.context.annotation.Prototype

@Prototype
actual class PingService : IPingService {

    override suspend fun ping(message: String): String {
        println(message)
        return "Hello world from server!"
    }
}
