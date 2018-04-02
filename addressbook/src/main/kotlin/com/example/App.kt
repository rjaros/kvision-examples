package com.example

import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.SplitPanel.Companion.splitPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.vh

class App : ApplicationBase {

    override fun start(state: Map<String, Any>) {
        Model.loadAddresses()
        Root("kvapp") {
            splitPanel {
                height = 100.vh
                add(ListPanel)
                add(EditPanel)
            }
        }
    }

    override fun dispose(): Map<String, Any> {
        return mapOf()
    }

    companion object {
        val css = require("./css/kvapp.css")
    }
}
