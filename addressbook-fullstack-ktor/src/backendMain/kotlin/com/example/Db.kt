package com.example

import com.axiomalaska.jdbc.NamedParameterPreparedStatement
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.sql.*


object Db {

    fun init(config: ApplicationConfig) {
        Database.connect(hikari(config))
        transaction {
            create(UserDao)
            create(AddressDao)
        }
    }

    private fun hikari(config: ApplicationConfig): HikariDataSource {
        val hikariConfig = HikariConfig()
        hikariConfig.driverClassName = config.propertyOrNull("db.driver")?.getString() ?: "org.h2.Driver"
        hikariConfig.jdbcUrl = config.propertyOrNull("db.jdbcUrl")?.getString() ?: "jdbc:h2:mem:test"
        hikariConfig.username = config.propertyOrNull("db.username")?.getString()
        hikariConfig.password = config.propertyOrNull("db.password")?.getString()
        hikariConfig.maximumPoolSize = 3
        hikariConfig.isAutoCommit = false
        hikariConfig.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }

    suspend fun <T> dbQuery(block: Transaction.() -> T): T = withContext(Dispatchers.IO) {
        transaction {
            block()
        }
    }

    fun <T : Any> Transaction.queryList(
        query: String,
        parameters: Map<String, Any?>,
        transform: (ResultSet) -> T
    ): List<T> {
        val statement = NamedParameterPreparedStatement.createNamedParameterPreparedStatement(connection, query)
        statement.setParameters(parameters)
        val result = arrayListOf<T>()
        val resultSet = statement.executeQuery()
        resultSet.use {
            while (resultSet.next()) {
                result += transform(resultSet)
            }
        }
        return result
    }

    fun <T : Any> Transaction.queryObject(
        query: String,
        parameters: Map<String, Any?>,
        transform: (ResultSet) -> T
    ): T? {
        val statement = NamedParameterPreparedStatement.createNamedParameterPreparedStatement(connection, query)
        statement.setParameters(parameters)
        val resultSet = statement.executeQuery()
        resultSet.use {
            if (resultSet.next()) {
                return transform(resultSet)
            }
        }
        return null
    }

    private fun NamedParameterPreparedStatement.setParameters(parameters: Map<String, Any?>) {
        parameters.forEach { key, value ->
            when (value) {
                null -> setNull(key, Types.NULL)
                is String -> setString(key, value)
                is Boolean -> setBoolean(key, value)
                is Int -> setInt(key, value)
                is Byte -> setByte(key, value)
                is Long -> setLong(key, value)
                is Short -> setShort(key, value)
                is Float -> setFloat(key, value)
                is Double -> setDouble(key, value.toFloat())
                is BigDecimal -> setBigDecimal(key, value)
                is Date -> setDate(key, value)
                is Time -> setTime(key, value)
                is Timestamp -> setTimestamp(key, value)
                is ByteArray -> setBytes(key, value)
                else -> setObject(key, value)
            }
        }
    }
}

