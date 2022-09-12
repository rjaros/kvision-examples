package com.example

import com.github.andrewoma.kwery.core.ThreadLocalSession
import com.github.andrewoma.kwery.core.interceptor.LoggingInterceptor
import com.typesafe.config.Config
import io.jooby.hikari.HikariModule
import io.jooby.pac4j.Pac4jModule
import io.jooby.pac4j.Pac4jOptions
import io.jooby.require
import io.jooby.runApp
import io.kvision.remote.applyRoutes
import io.kvision.remote.getServiceManager
import io.kvision.remote.kvisionInit
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.http.client.indirect.FormClient
import javax.sql.DataSource

fun main(args: Array<String>) {
    runApp(args) {
        kvisionInit()
        install(HikariModule("db"))
        applyRoutes(getServiceManager<IRegisterProfileService>())
        val config = org.pac4j.core.config.Config()
        config.addAuthorizer("Authorizer") { _, _ -> true }
        install(Pac4jModule(Pac4jOptions().apply {
            defaultUrl = "/"
        }, config).client("*", "Authorizer") { _ ->
            FormClient("/") { credentials, context ->
                require(MyDbProfileService::class).validate(credentials as UsernamePasswordCredentials, context)
            }
        })
        applyRoutes(getServiceManager<IAddressService>())
        applyRoutes(getServiceManager<IProfileService>())
        onStarted {
            val db = require(DataSource::class, "db")
            val session = ThreadLocalSession(db, getDbDialect(require(Config::class)), LoggingInterceptor())
            try {
                AddressDao(session).findById(1)
            } catch (e: Exception) {
                val schema = this.javaClass.getResource("/schema.sql").readText()
                session.update(schema)
            }
        }
    }
}
