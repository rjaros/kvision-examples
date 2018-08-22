package com.example

import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.i18n.I18n.gettext
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.require

object App : ApplicationBase {

    private lateinit var root: Root

    override fun start(state: Map<String, Any>) {
        root = Root("kvapp") {
            println(gettext("This is a localized message."))
        }
    }

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }

    val css = require("./css/kvapp.css")
}
