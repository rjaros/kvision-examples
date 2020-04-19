package com.example

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pl.treksoft.kvision.remote.applyRoutes
import pl.treksoft.kvision.remote.kvisionInit
import pl.treksoft.kvision.remote.serviceRoute

const val SESSION_PROFILE_KEY = "com.example.profile"

class MainVerticle : AbstractVerticle() {
    override fun start() {
        val router = Router.router(vertx)
        vertx.kvisionInit(router, ConfigModule(), DbModule())
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)))
        val authHandler = MyAuthHandler()
        router.serviceRoute(AddressServiceManager, authHandler)
        router.serviceRoute(ProfileServiceManager, authHandler)
        vertx.applyRoutes(router, AddressServiceManager)
        vertx.applyRoutes(router, ProfileServiceManager)
        vertx.applyRoutes(router, RegisterProfileServiceManager)
        router.route(HttpMethod.POST, "/login").handler(BodyHandler.create(false)).blockingHandler { rctx ->
            val username = rctx.request().getFormAttribute("username") ?: ""
            val password = rctx.request().getFormAttribute("password") ?: ""
            transaction {
                UserDao.select {
                    (UserDao.username eq username) and (UserDao.password eq DigestUtils.sha256Hex(password))
                }.firstOrNull()?.let {
                    val profile =
                        Profile(it[UserDao.id], it[UserDao.name], it[UserDao.username].toString(), null, null)
                    rctx.session().put(SESSION_PROFILE_KEY, profile)
                    rctx.response().setStatusCode(200).end()
                } ?: rctx.response().setStatusCode(401).end()
            }
        }
        router.route(HttpMethod.GET, "/logout").handler { rctx ->
            rctx.clearUser()
            rctx.session().destroy()
            rctx.response().putHeader("Location", "/").setStatusCode(302).end()
        }
        vertx.createHttpServer().requestHandler(router).listen(8080)
    }
}
