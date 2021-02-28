package com.example

import io.kvision.Application
import io.kvision.html.div
import io.kvision.module
import io.kvision.panel.root
import io.kvision.panel.simplePanel
import io.kvision.require
import io.kvision.startApplication
import io.kvision.utils.px

class App : Application() {

    init {
        require("fomantic-ui-css/semantic.min.css")
        require("fomantic-ui-css/semantic.min.js")
        require("css/kvapp.css")
    }

    override fun start(state: Map<String, Any>) {
        root("kvapp") {
            padding = 10.px
            simplePanel(Model.store) { state ->
                div(className = "ui fluid container") {
                    div(className = "ui segment") {
                        toolbar(state)
                        cardView(state)
                    }
                }
            }
        }
        randomUsers(73).then {
            Model.setUsers(it)
        }
    }
}

fun main() {
    startApplication(::App, module.hot)
}
