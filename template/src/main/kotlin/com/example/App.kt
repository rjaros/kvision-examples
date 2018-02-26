package com.example

import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.panel.Root

class App : ApplicationBase {

    override fun start(state: Map<String, Any>) {
        Root("kvapp") {
        // TODO
        }
    }

    override fun dispose(): Map<String, Any> {
        return mapOf()
    }

    companion object {
        val css = require("./css/kvapp.css")
    }
}
