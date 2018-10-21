package com.example

import org.jooby.Jooby.run
import pl.treksoft.kvision.remote.KVServer

class App : KVServer({
    NumberServiceManager.applyRoutes(this)
})

fun main(args: Array<String>) {
    run(::App, args)
}
