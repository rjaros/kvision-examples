package com.example

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.runtime.Micronaut
import io.kvision.remote.KVManagers
import io.kvision.remote.getAllServiceManagers

@Factory
class KVApplication {
    @Bean
    fun getManagers() = KVManagers(getAllServiceManagers())
}

fun main(args: Array<String>) {
    Micronaut.build()
        .args(*args)
        .packages("com.example")
        .start()
}
