package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.form.select.Select
import pl.treksoft.kvision.form.select.Select.Companion.select
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.SplitPanel.Companion.splitPanel
import pl.treksoft.kvision.utils.vh

object App : ApplicationBase {

    private lateinit var root: Root

    override fun start(state: Map<String, Any>) {
        I18n.manager =
                DefaultI18nManager(
                        mapOf(
                                "en" to pl.treksoft.kvision.require("i18n/messages-en.json"),
                                "pl" to pl.treksoft.kvision.require("i18n/messages-pl.json")
                        )
                )
        root = Root("kvapp") {
            select(options = listOf("en" to "English", "pl" to "Polskie"), value = I18n.language, label = tr("Language")) {
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
            }
        }
        GlobalScope.launch {
            Model.getAddressList()
        }
    }

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }
}
