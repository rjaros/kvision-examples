package com.example

import io.kvision.Application
import io.kvision.Hot
import io.kvision.panel.root
import io.kvision.panel.simplePanel
import io.kvision.startApplication
import io.kvision.state.bind
import io.kvision.utils.px
import io.kvision.utils.useModule

@JsModule("@patternfly/patternfly/patternfly.min.css")
external val patternflyCss: dynamic

@JsModule("@patternfly/patternfly/patternfly-addons.css")
external val patternflyAddonsCss: dynamic

@JsModule("/kotlin/modules/css/kvapp.css")
external val kvappCss: dynamic

class App : Application() {

    init {
        useModule(patternflyCss)
        useModule(patternflyAddonsCss)
        useModule(kvappCss)
    }

    override fun start() {
        root("kvapp") {
            padding = 10.px
            simplePanel().bind(Model.store) { state ->
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
    startApplication(::App, js("import.meta.webpackHot").unsafeCast<Hot?>())
}
