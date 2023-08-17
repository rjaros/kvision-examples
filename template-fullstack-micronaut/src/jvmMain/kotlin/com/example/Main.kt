package com.example

import io.kvision.remote.KVManagers
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.runtime.Micronaut.run

@Factory
class KVApplication {
    @Bean
    fun getManagers() = KVManagers(listOf(PingServiceManager))
}

fun main(args: Array<String>) {
    run(*args)
}
