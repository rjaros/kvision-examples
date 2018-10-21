package com.example

import kotlinx.coroutines.Deferred
import org.springframework.stereotype.Service
import pl.treksoft.kvision.remote.async

@Service
actual class PingService actual constructor() {

    actual fun ping(message: String): Deferred<String> = async {
        println(message)
        "Hello world from server!"
    }
}
