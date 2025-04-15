package com.example

import dev.kilua.rpc.getAllServiceManagers
import io.kvision.remote.registerRemoteTypes
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity

@EnableR2dbcRepositories
@SpringBootApplication
@EnableWebFluxSecurity
@EnableAutoConfiguration(
    exclude = [
        org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration::class,
        org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration::class
    ]
)
class KVApplication {

    @Value("classpath:schema.sql")
    lateinit var schema: Resource

    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        initializer.setDatabasePopulator(ResourceDatabasePopulator(schema))
        return initializer
    }

    @Bean
    fun getManagers() = getAllServiceManagers()
}

fun main(args: Array<String>) {
    registerRemoteTypes()
    runApplication<KVApplication>(*args)
}
