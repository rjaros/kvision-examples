package com.example

import io.kvision.Application
import io.kvision.Hot
import io.kvision.html.div
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
    startApplication(::App, js("import.meta.webpackHot").unsafeCast<Hot?>())
}
