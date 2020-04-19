package com.example

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.User
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.AuthHandler
import io.vertx.ext.web.handler.impl.HttpStatusException


class MyUser(val profile: Profile) : User {
    override fun clearCache(): User {
        return this
    }

    override fun setAuthProvider(authProvider: AuthProvider) {
    }

    override fun isAuthorized(authority: String, resultHandler: Handler<AsyncResult<Boolean>>): User {
        resultHandler.handle(Future.succeededFuture(true))
        return this
    }

    override fun principal(): JsonObject {
        return JsonObject().put("username", profile.username)
    }
}

class MyAuthHandler : AuthHandler {
    override fun addAuthority(authority: String): AuthHandler {
        return this
    }

    override fun parseCredentials(context: RoutingContext, handler: Handler<AsyncResult<JsonObject>>) {
        handler.handle(
            Future.failedFuture(HttpStatusException(401))
        )
    }

    override fun handle(rctx: RoutingContext) {
        val user = rctx.user()
        if (user != null) {
            rctx.next()
            return
        }
        val profile = rctx.session().get<Profile>(SESSION_PROFILE_KEY)
        if (profile != null) {
            rctx.setUser(MyUser(profile))
            rctx.next()
            return
        }
        rctx.fail(401)
    }

    override fun addAuthorities(authorities: MutableSet<String>): AuthHandler {
        return this
    }

    override fun authorize(user: User, handler: Handler<AsyncResult<Void>>) {
        handler.handle(Future.succeededFuture())
    }
}
