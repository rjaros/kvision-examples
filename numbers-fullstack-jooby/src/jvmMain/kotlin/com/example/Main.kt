package com.example

import io.jooby.kt.runApp
import io.kvision.remote.applyRoutes
import io.kvision.remote.kvisionInit

fun main(args: Array<String>) {
    runApp(args) {
        kvisionInit()
        applyRoutes(NumberServiceManager)
    }
}
