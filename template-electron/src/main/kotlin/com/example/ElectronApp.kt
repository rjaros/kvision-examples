package com.example

import pl.treksoft.kvision.Application
import pl.treksoft.kvision.electron.BrowserWindow
import pl.treksoft.kvision.electron.Remote
import pl.treksoft.kvision.electron.nodejs.Process
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.Root.Companion.root
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.utils.createInstance
import pl.treksoft.kvision.utils.obj
import pl.treksoft.kvision.utils.px
import kotlin.browser.window

external val process: Process

class ElectronApp : Application() {

    val remote: Remote = require("electron").remote
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
            vPanel(alignItems = FlexAlignItems.CENTER, spacing = 30) {
                marginTop = 50.px
                fontSize = 30.px
                hPanel(spacing = 20) {
                    div(tr("Electron version"))
                    div("${process.versions?.electron}")
                }
                hPanel(spacing = 20) {
                    div(tr("Chrome version"))
                    div("${process.versions?.chrome}")
                }
            }
        }

        nativeWindow = remote.BrowserWindow.createInstance(obj {
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
