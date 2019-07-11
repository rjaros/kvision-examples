package com.example

import org.jooby.Jooby.run
import org.jooby.Kooby
import pl.treksoft.kvision.remote.applyRoutes
import pl.treksoft.kvision.remote.kvisionInit

class App : Kooby({
    kvisionInit()
    applyRoutes(PingServiceManager)
    onStart {
        // Initialization
    }
})

fun main(args: Array<String>) {
    run(::App, args)
}
