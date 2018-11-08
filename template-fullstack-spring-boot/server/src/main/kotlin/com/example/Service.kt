package com.example

import org.springframework.stereotype.Service
import pl.treksoft.kvision.remote.async

@Service
actual class PingService : IPingService {

    override fun ping(message: String) = async {
        println(message)
        "Hello world from server!"
    }
}
