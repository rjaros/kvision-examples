package com.example

import kotlinx.coroutines.experimental.launch
import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.Label
import pl.treksoft.kvision.panel.Root

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
object App : ApplicationBase {

    private lateinit var root: Root

    override fun start(state: Map<String, Any>) {
        root = Root("kvapp") {
        }
        launch {
            val pingResult = Model.ping("Hello world from client!")
            root.add(Label(pingResult))
        }
    }

    override fun dispose(): Map<String, Any> {
        root.dispose()
        return mapOf()
    }
}
