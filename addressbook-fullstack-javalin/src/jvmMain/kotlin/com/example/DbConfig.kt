package com.example

import com.google.inject.AbstractModule
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import jakarta.inject.Singleton
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*


@Singleton
class DbConfig {

    init {
        val prop = Properties()
        prop.load(this::class.java.getResourceAsStream("/application.properties"))
        Database.connect(
            hikari(
                prop.getProperty("db.driver"),
                prop.getProperty("db.jdbcUrl"),
                prop.getProperty("db.username"),
                prop.getProperty("db.password")
            )
        )
        transaction {
            SchemaUtils.create(UserDao)
            SchemaUtils.create(AddressDao)
        }
    }

    private fun hikari(dbDriver: String, dbJdbcUrl: String, dbUsername: String, dbPassword: String): HikariDataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = dbDriver
        hikariConfig.jdbcUrl = dbJdbcUrl
        hikariConfig.username = dbUsername
        hikariConfig.password = dbPassword
        hikariConfig.maximumPoolSize = 3
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }
}

class DbModule : AbstractModule() {
    override fun configure() {
        bind(DbConfig::class.java).asEagerSingleton()
    }
}
