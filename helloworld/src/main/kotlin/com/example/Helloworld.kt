package com.example

import pl.treksoft.kvision.hmr.ApplicationBase
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag.Companion.tag
import pl.treksoft.kvision.panel.FlexDir
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.FlexPanel.Companion.flexPanel
import pl.treksoft.kvision.panel.Root
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.px

class Helloworld : ApplicationBase {

    override fun start(state: Map<String, Any>) {
        Root("helloworld") {
            flexPanel(FlexDir.ROW, justify = FlexJustify.CENTER) {
                tag(TAG.DIV, "Hello world!", classes = setOf("helloworld")) {
                    marginTop = 50.px
                    fontSize = 50.px
                }
            }
        }
    }

    override fun dispose(): Map<String, Any> {
        return mapOf()
    }

    companion object {
        val css = require("./css/helloworld.css")
    }
}
