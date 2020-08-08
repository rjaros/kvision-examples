package com.example

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.runtime.Micronaut.build
import pl.treksoft.kvision.remote.KVManagers

@Factory
class KVApplication {
    @Bean
    fun getManagers() = KVManagers(listOf(PingServiceManager))
}

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("com.example")
        .start()
}
