package com.example

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
actual class PingService : IPingService {

    override suspend fun ping(message: String): String {
        println(message)
        return "Hello world from server!"
    }
}
