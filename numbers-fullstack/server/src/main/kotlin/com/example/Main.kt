package com.example

import org.jooby.Jooby.run
import pl.treksoft.kvision.remote.JoobyServer

class App : JoobyServer({
    NumberServiceManager.applyRoutes(this)
})

fun main(args: Array<String>) {
    run(::App, args)
}
