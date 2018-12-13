package com.example

import org.springframework.stereotype.Service

@Service
actual class PingService : IPingService {

    override suspend fun ping(message: String): String {
        println(message)
        return "Hello world from server!"
    }
}
