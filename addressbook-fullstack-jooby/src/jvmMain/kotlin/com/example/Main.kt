package com.example

import com.github.andrewoma.kwery.core.ThreadLocalSession
import com.github.andrewoma.kwery.core.interceptor.LoggingInterceptor
import com.typesafe.config.Config
import io.jooby.hikari.HikariModule
import io.jooby.kt.require
import io.jooby.kt.runApp
import io.jooby.pac4j.Pac4jModule
import io.jooby.pac4j.Pac4jOptions
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

        install(Pac4jModule(Pac4jOptions().apply {
            addAuthorizer("Authorizer") { _, _, _ -> true }
            defaultUrl = "/"
        }).client("*", "Authorizer") { _ ->
            FormClient("/") { context, credentials ->
                require(MyDbProfileService::class).validate(context, credentials as UsernamePasswordCredentials)
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
