package com.example

import io.javalin.Javalin
import io.javalin.http.HttpStatus
import io.javalin.security.RouteRole
import io.kvision.remote.applyRoutes
import io.kvision.remote.kvisionInit
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

const val SESSION_PROFILE_KEY = "com.example.profile"

enum class ApiRole : RouteRole { AUTHORIZED, ANYONE }

fun main() {
    Javalin.create { config ->
        config.accessManager { handler, ctx, permittedRoles ->
            when {
                permittedRoles.contains(ApiRole.ANYONE) -> handler.handle(ctx)
                ctx.sessionAttribute<Profile>(SESSION_PROFILE_KEY) != null -> handler.handle(ctx)
                else -> ctx.status(HttpStatus.UNAUTHORIZED).json("Unauthorized")
            }
        }
    }.start(8080).apply {
        kvisionInit(DbModule())
        applyRoutes(AddressServiceManager, setOf(ApiRole.AUTHORIZED))
        applyRoutes(ProfileServiceManager, setOf(ApiRole.AUTHORIZED))
        applyRoutes(RegisterProfileServiceManager, setOf(ApiRole.ANYONE))

        // Security config
        post("/login", { ctx ->
            val username = ctx.formParam("username") ?: ""
            val password = ctx.formParam("password") ?: ""
            transaction {
                UserDao.select {
                    (UserDao.username eq username) and (UserDao.password eq DigestUtils.sha256Hex(password))
                }.firstOrNull()?.let {
                    val profile =
                        Profile(it[UserDao.id], it[UserDao.name], it[UserDao.username].toString(), null, null)
                    ctx.sessionAttribute(SESSION_PROFILE_KEY, profile)
                    ctx.status(HttpStatus.OK)
                } ?: ctx.status(HttpStatus.UNAUTHORIZED)
            }
        }, ApiRole.ANYONE)
        get("/logout", { ctx ->
            ctx.req().session.invalidate()
            ctx.redirect("/", HttpStatus.FOUND)
        }, ApiRole.AUTHORIZED)
    }
}
