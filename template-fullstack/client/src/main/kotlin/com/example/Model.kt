package com.example

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
object Model {

    private val pingService = PingService()

    suspend fun ping(message: String): String {
        return pingService.ping(message).await()
    }

}
