package io.ktor.samples.fullstack.mpp

actual class PingService : IPingService {
    override suspend fun ping(message: String): String {
        println(message)
        return "Hello world from Server!"
    }
}
