package com.example

import kotlinx.serialization.UnstableDefault
import pl.treksoft.kvision.form.select.Select
import pl.treksoft.kvision.form.select.Select.Companion.select
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.SplitPanel.Companion.splitPanel
import pl.treksoft.kvision.require
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
            select(options = listOf("en" to "English", "pl" to "Polskie"), value = I18n.language, label = I18n.tr("Language")) {
                setEventListener<Select> {
                    change = {
                        self.value?.let { I18n.language = it }
                    }
                }
            }
            splitPanel {
                height = 100.vh
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
