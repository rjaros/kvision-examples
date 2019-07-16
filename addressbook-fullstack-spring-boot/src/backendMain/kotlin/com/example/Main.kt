package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class KVApplication {
    @Bean
    fun getManagers() = listOf(RegisterProfileServiceManager, ProfileServiceManager, AddressServiceManager)
}

fun main(args: Array<String>) {
    runApplication<KVApplication>(*args)
}
