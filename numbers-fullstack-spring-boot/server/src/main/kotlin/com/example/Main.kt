package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import pl.treksoft.kvision.remote.KVServer

@SpringBootApplication
@ComponentScan(basePackages = ["com.example", "pl.treksoft.kvision.remote"])
class KVApplication {
    @Bean
    fun getKVServer(): KVServer {
        return KVServer(listOf(NumberServiceManager))
    }
}

fun main(args: Array<String>) {
    runApplication<KVApplication>(*args)
}
