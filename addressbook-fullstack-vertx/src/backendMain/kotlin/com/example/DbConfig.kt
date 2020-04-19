package com.example

import com.google.inject.AbstractModule
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbConfig @Inject constructor(config: Config) {

    init {
        Database.connect(hikari(config))
        transaction {
            SchemaUtils.create(UserDao)
            SchemaUtils.create(AddressDao)
        }
    }

    private fun hikari(config: Config): HikariDataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = config.dbDriver
        hikariConfig.jdbcUrl = config.dbJdbcUrl
        hikariConfig.username = config.dbUsername
        hikariConfig.password = config.dbPassword
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
