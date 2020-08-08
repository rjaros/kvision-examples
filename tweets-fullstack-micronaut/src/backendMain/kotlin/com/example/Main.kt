package com.example

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.runtime.Micronaut
import pl.treksoft.kvision.remote.KVManagers

@Factory
class KVApplication {
    @Bean
    fun getManagers() = KVManagers(listOf(TweetServiceManager))
}

fun main(args: Array<String>) {
    Micronaut.build()
        .args(*args)
        .packages("com.example")
        .start()
}
