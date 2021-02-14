package com.example

import io.kvision.Application
import io.kvision.module
import io.kvision.panel.root
import io.kvision.panel.simplePanel
import io.kvision.require
import io.kvision.startApplication
import io.kvision.utils.px

class App : Application() {

    init {
        require("@patternfly/patternfly/patternfly.min.css")
        require("@patternfly/patternfly/patternfly-addons.css")
        require("css/kvapp.css")
    }

    override fun start(state: Map<String, Any>) {
        root("kvapp") {
            padding = 10.px
            simplePanel(Model.store) { state ->
                toolbar(state)
                when (state.view) {
                    ViewType.CARD -> {
                        cardView(state)
                    }
                    ViewType.LIST -> {
                        listView(state)
                    }
                    ViewType.TABLE -> {
                        tableView(state)
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
