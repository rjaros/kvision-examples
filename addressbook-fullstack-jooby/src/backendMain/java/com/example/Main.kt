package com.example

import com.github.andrewoma.kwery.core.ThreadLocalSession
import com.github.andrewoma.kwery.core.interceptor.LoggingInterceptor
import com.typesafe.config.Config
import io.jooby.hikari.HikariModule
import io.jooby.pac4j.Pac4jModule
import io.jooby.require
import io.jooby.runApp
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.http.client.indirect.FormClient
import pl.treksoft.kvision.remote.applyRoutes
import pl.treksoft.kvision.remote.kvisionInit
import javax.sql.DataSource

fun main(args: Array<String>) {
    runApp(args) {
        kvisionInit()
        install(HikariModule("db"))
        applyRoutes(RegisterProfileServiceManager)
        install(Pac4jModule().client { _ ->
            FormClient("/") { credentials, context ->
                require(MyDbProfileService::class).validate(credentials as UsernamePasswordCredentials, context)
            }
        })
        applyRoutes(AddressServiceManager)
        applyRoutes(ProfileServiceManager)
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
