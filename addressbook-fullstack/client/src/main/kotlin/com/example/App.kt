package com.example

import kotlinx.coroutines.experimental.launch
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.panel.SplitPanel.Companion.splitPanel
import pl.treksoft.kvision.utils.vh

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
object App : ApplicationBase {

    private lateinit var root: Root

    override fun start(state: Map<String, Any>) {
        root = Root("kvapp") {
            splitPanel {
                height = 100.vh
                add(ListPanel)
                add(EditPanel)
            }
        }
        launch {
            Model.getAddressList()
        }
    }

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }
}
