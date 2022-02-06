package com.example

import io.kvision.Application
import io.kvision.BootstrapCssModule
import io.kvision.BootstrapModule
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.core.AlignItems
import io.kvision.electron.global
import io.kvision.html.div
import io.kvision.i18n.DefaultI18nManager
import io.kvision.i18n.I18n
import io.kvision.i18n.I18n.tr
import io.kvision.module
import io.kvision.panel.hPanel
import io.kvision.panel.root
import io.kvision.panel.vPanel
import io.kvision.require
import io.kvision.startApplication
import io.kvision.utils.perc
import io.kvision.utils.px
import kotlinx.browser.window

class ElectronApp : Application() {

    override fun start() {
        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "en" to require("i18n/messages-en.json"),
                    "pl" to require("i18n/messages-pl.json")
                )
            )

        root("kvapp") {
            vPanel(alignItems = AlignItems.CENTER, spacing = 30) {
                width = 100.perc
                marginTop = 50.px
                fontSize = 30.px
                hPanel(spacing = 20) {
                    div(tr("Electron version"))
                    div(global.process.versions.electron)
                }
                hPanel(spacing = 20) {
                    div(tr("Chrome version"))
                    div(global.process.versions.chrome)
                }
            }
        }

        window.onunload = {
        }
    }

    override fun dispose(): Map<String, Any> {
        super.dispose()
        return mapOf()
    }
}

fun main() {
    startApplication(::ElectronApp, module.hot, BootstrapModule, BootstrapCssModule, FontAwesomeModule, CoreModule)
}
