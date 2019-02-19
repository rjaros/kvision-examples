package io.ktor.samples.fullstack.mpp

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.script
import pl.treksoft.kvision.remote.applyRoutes
import pl.treksoft.kvision.remote.kvisionInit
import java.io.File

fun Application.main() {
    kvisionInit()

    val currentDir = File(".").absoluteFile
    val webDir = listOf(
        "build/web"
    ).map {
        File(currentDir, it)
    }.firstOrNull { it.isDirectory }?.absoluteFile ?: error("Can't find 'web' folder for this sample")

    routing {
        applyRoutes(PingServiceManager)
        get("/") {
            call.respondHtml {
                body {
                    div {
                        id = "js-response"
                        +"Loading..."
                    }
                    script(src = "/static/require.min.js") {
                    }
                    script {
                        +"require.config({baseUrl: '/static'});\n"
                        +"require(['/static/frontend.js'], function(frontend) { frontend.io.ktor.samples.fullstack.mpp.app(); });\n"
                    }
                }
            }
        }
        static("/static") {
            files(webDir)
        }
    }
}

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) { main() }.start(wait = true)
}
