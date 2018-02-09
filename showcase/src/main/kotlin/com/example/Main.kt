package com.example

import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.hmr.module
import pl.treksoft.kvision.routing.Routing
import kotlin.browser.document

external fun require(name: String): String

fun main(args: Array<String>) {
    var application: ApplicationBase? = null

    val state: dynamic = module.hot?.let { hot ->
        hot.accept()

        hot.dispose { data ->
            data.appState = application?.dispose()
            application = null
        }

        hot.data
    }

    if (document.body != null) {
        Routing.start()
        application = start(state)
    } else {
        application = null
        document.addEventListener("DOMContentLoaded", { application = start(state) })
    }
}

fun start(state: dynamic): ApplicationBase? {
    if (document.getElementById("showcase") == null) return null
    val application = Showcase()
    @Suppress("UnsafeCastFromDynamic")
    application.start(state?.appState ?: emptyMap())
    return application
}

