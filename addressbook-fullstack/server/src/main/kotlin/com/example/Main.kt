package com.example

import com.github.andrewoma.kwery.core.ThreadLocalSession
import com.github.andrewoma.kwery.core.interceptor.LoggingInterceptor
import com.typesafe.config.Config
import org.jooby.Jooby.run
import org.jooby.jdbc.Jdbc
import org.jooby.pac4j.Pac4j
import org.jooby.require
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.credentials.password.SpringSecurityPasswordEncoder
import org.pac4j.http.client.indirect.FormClient
import org.pac4j.sql.profile.service.DbProfileService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pl.treksoft.kvision.remote.KVServer
import javax.inject.Inject
import javax.sql.DataSource

class MyDbProfileService @Inject constructor(ds: DataSource) :
    DbProfileService(ds, SpringSecurityPasswordEncoder(BCryptPasswordEncoder()))

class App : KVServer({
    use(Jdbc("db"))
    RegisterProfileServiceManager.applyRoutes(this)
    use(Pac4j().client { _ ->
        FormClient("/") { credentials, context ->
            require(MyDbProfileService::class).validate(credentials as UsernamePasswordCredentials, context)
        }
    })
    AddressServiceManager.applyRoutes(this)
    ProfileServiceManager.applyRoutes(this)
    onStart {
        val db = require("db", DataSource::class)
        val session = ThreadLocalSession(db, getDbDialect(require(Config::class)), LoggingInterceptor())
        try {
            AddressDao(session).findById(1)
        } catch (e: Exception) {
            val schema = this.javaClass.getResource("/schema.sql").readText()
            session.update(schema)
        }
    }
})

fun main(args: Array<String>) {
    run(::App, args)
}
