package com.example

import io.kvision.remote.kvisionInit
import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router

class MainVerticle : AbstractVerticle() {
    override fun start() {
        val router = Router.router(vertx)
        val server = vertx.createHttpServer()
        vertx.kvisionInit(router, server, listOf(TweetServiceManager))
        server.requestHandler(router).listen(8080)
    }
}
