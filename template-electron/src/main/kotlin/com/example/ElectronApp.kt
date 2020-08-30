package com.example

import kotlinx.browser.window
import pl.treksoft.kvision.Application
import pl.treksoft.kvision.core.AlignItems
import pl.treksoft.kvision.electron.electron.BrowserWindow
import pl.treksoft.kvision.electron.electron.BrowserWindowConstructorOptions
import pl.treksoft.kvision.electron.electron.remote
import pl.treksoft.kvision.electron.global
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.utils.createInstance
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px

class ElectronApp : Application() {

    private var nativeWindow: BrowserWindow? = null

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

        nativeWindow =
            remote.BrowserWindow.createInstance<BrowserWindow>(object : BrowserWindowConstructorOptions {}.apply {
                title = "Native window"
                width = 700
                height = 500
            })
        nativeWindow?.on("closed")
        { _ ->
            nativeWindow = null
        }
        window.onunload = {
            nativeWindow?.destroy()
            nativeWindow = null
            Unit
        }
    }

    override fun dispose(): Map<String, Any> {
        super.dispose()
        nativeWindow?.destroy()
        nativeWindow = null
        return mapOf()
    }
}

fun main() {
    startApplication(::ElectronApp)
}
