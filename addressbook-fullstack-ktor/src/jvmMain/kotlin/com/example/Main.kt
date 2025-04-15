package com.example

import com.example.Db.dbQuery
import dev.kilua.rpc.getServiceManager
import dev.kilua.rpc.initRpc
import dev.kilua.rpc.registerService
import dev.kilua.rpc.applyRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.kvision.remote.registerRemoteTypes
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

fun Application.main() {
    registerRemoteTypes()
    install(Compression)
    install(DefaultHeaders)
    install(CallLogging)
    install(Sessions) {
        cookie<Profile>("KTSESSION", storage = SessionStorageMemory()) {
            cookie.path = "/"
            cookie.extensions["SameSite"] = "strict"
        }
    }
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
        applyRoutes(getServiceManager<IRegisterProfileService>())
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
            applyRoutes(getServiceManager<IAddressService>())
            applyRoutes(getServiceManager<IProfileService>())
        }
    }
    initRpc {
        registerService<IAddressService> { AddressService(it) }
        registerService<IProfileService> { ProfileService(it) }
        registerService<IRegisterProfileService> { RegisterProfileService() }
    }
}
