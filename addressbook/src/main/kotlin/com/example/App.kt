package com.example

import kotlinx.serialization.UnstableDefault
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.SplitPanel.Companion.splitPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.vh

@UnstableDefault
object App : ApplicationBase {
    init {
        require("css/kvapp.css")
    }

    private lateinit var root: Root

    override fun start(state: Map<String, Any>) {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to require("i18n/messages-en.json"),
                    "pl" to require("i18n/messages-pl.json")
                )
            )
        root = Root("kvapp") {
            splitPanel {
                height = 100.vh
                width = 100.perc
                add(ListPanel)
                add(EditPanel)
                Model.loadAddresses()
            }
        }
    }

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }
}
