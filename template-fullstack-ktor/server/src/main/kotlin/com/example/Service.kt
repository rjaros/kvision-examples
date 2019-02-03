package com.example

import com.google.inject.Singleton

@Singleton
actual class PingService : IPingService {

    override suspend fun ping(message: String): String {
        println(message)
        return "Hello world from server!"
    }
}
