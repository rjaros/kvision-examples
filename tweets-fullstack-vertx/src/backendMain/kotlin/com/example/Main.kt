package com.example

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import pl.treksoft.kvision.remote.kvisionInit

class MainVerticle : AbstractVerticle() {
    override fun start() {
        val router = Router.router(vertx)
        val server = vertx.createHttpServer()
        vertx.kvisionInit(router, server, listOf(TweetServiceManager))
        server.requestHandler(router).listen(8080)
    }
}
