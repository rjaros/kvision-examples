package com.example

import io.kvision.Application
import io.kvision.html.div
import io.kvision.module
import io.kvision.panel.root
import io.kvision.startApplication

class App : Application() {

    override fun start() {
        root("kvapp") {
            div("Hello KVision!")
        }
    }
}

fun main() {
    startApplication(::App, module.hot)
}
