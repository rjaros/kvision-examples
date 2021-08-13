package com.example

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Property
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import jakarta.inject.Singleton
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.mapping.SettableValue

@Factory
class R2dbc {

    @field:Property(name = "spring.r2dbc.url")
    lateinit var r2dbcUrl: String

    @Singleton
    fun getConnectionFactory(): ConnectionFactory {
        return ConnectionFactories.get(r2dbcUrl)
    }

    @Singleton
    fun getDatabaseClient(connectionFactory: ConnectionFactory): DatabaseClient {
        return DatabaseClient.create(connectionFactory)
    }
}

@Singleton
class R2dbcInitializer(val connectionFactory: ConnectionFactory) {

    @EventListener
    internal fun onStartupEvent(event: StartupEvent) {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        initializer.setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
        initializer.afterPropertiesSet()
    }

}

fun DatabaseClient.GenericExecuteSpec.bindMap(parameters: Map<String, Any?>): DatabaseClient.GenericExecuteSpec {
    return parameters.entries.fold(this) { spec, entry ->
        if (entry.value == null) {
            spec.bindNull(entry.key, String::class.java)
        } else {
            spec.bind(entry.key, SettableValue.fromOrEmpty(entry.value, entry.value!!::class.java))
        }
    }
}
