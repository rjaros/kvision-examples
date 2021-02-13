package com.example

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import io.kvision.remote.applyRoutes
import io.kvision.remote.kvisionInit

class MainVerticle : AbstractVerticle() {
    override fun start() {
        val router = Router.router(vertx)
        vertx.kvisionInit(router)
        vertx.applyRoutes(router, PingServiceManager)
        vertx.createHttpServer().requestHandler(router).listen(8080)
    }
}
