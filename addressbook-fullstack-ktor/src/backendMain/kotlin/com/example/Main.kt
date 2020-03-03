package com.example

import com.example.Db.dbQuery
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.form
import io.ktor.auth.principal
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.DefaultHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.Sessions
import io.ktor.sessions.clear
import io.ktor.sessions.cookie
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import pl.treksoft.kvision.remote.applyRoutes
import pl.treksoft.kvision.remote.kvisionInit
import kotlin.collections.set

fun Application.main() {
    install(Compression)
    install(DefaultHeaders)
    install(CallLogging)
    install(Sessions) {
        cookie<Profile>("SESSION", storage = SessionStorageMemory()) {
            cookie.path = "/"
            cookie.extensions["SameSite"] = "strict"
        }
    }
    kvisionInit()
    Db.init(environment.config)

    install(Authentication) {
        form {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                dbQuery {
                    UserDao.select {
                        (UserDao.username eq credentials.name) and (UserDao.password eq DigestUtils.sha256Hex(
                            credentials.password
                        ))
                    }.firstOrNull()?.let {
                        UserIdPrincipal(credentials.name)
                    }
                }
            }
            skipWhen { call -> call.sessions.get<Profile>() != null }
        }
    }

    routing {
        applyRoutes(RegisterProfileServiceManager)
        authenticate {
            post("login") {
                val principal = call.principal<UserIdPrincipal>()
                val result = if (principal != null) {
                    dbQuery {
                        UserDao.select { UserDao.username eq principal.name }.firstOrNull()?.let {
                            val profile =
                                Profile(it[UserDao.id], it[UserDao.name], it[UserDao.username].toString(), null, null)
                            call.sessions.set(profile)
                            HttpStatusCode.OK
                        } ?: HttpStatusCode.Unauthorized
                    }
                } else {
                    HttpStatusCode.Unauthorized
                }
                call.respond(result)
            }
            get("logout") {
                call.sessions.clear<Profile>()
                call.respondRedirect("/")
            }
            applyRoutes(AddressServiceManager)
            applyRoutes(ProfileServiceManager)
        }
    }
}
