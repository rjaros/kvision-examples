package com.example

import pl.treksoft.kvision.Application
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.module
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.startApplication

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
