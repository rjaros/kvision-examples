package com.example

import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.hmr.module
import pl.treksoft.kvision.i18n.I18n
import kotlin.browser.document

fun main(args: Array<String>) {

    I18n.init("en", "pl", "de", "es", "fr", "ru", "ja", "ko") {

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
            application = start(state)
        } else {
            application = null
            document.addEventListener("DOMContentLoaded", { application = start(state) })
        }
    }
}

fun start(state: dynamic): ApplicationBase? {
    if (document.getElementById("helloworld") == null) return null
    @Suppress("UnsafeCastFromDynamic")
    Helloworld.start(state?.appState ?: emptyMap())
    return Helloworld
}

