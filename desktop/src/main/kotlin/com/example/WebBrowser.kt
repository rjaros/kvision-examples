package com.example

import io.kvision.core.Container
import io.kvision.core.CssSize
import io.kvision.core.UNIT
import io.kvision.form.text.TextInput
import io.kvision.html.button
import io.kvision.html.Iframe
import io.kvision.navbar.navForm
import io.kvision.navbar.navbar
import io.kvision.toolbar.buttonGroup
import io.kvision.utils.perc
import io.kvision.utils.px

class WebBrowser : DesktopWindow("Web Browser", "fab fa-firefox", 900, 400) {

    override var height: CssSize?
        get() = super.height
        set(value) {
            super.height = value
            if (value?.second == UNIT.px) {
                iframe.height = (value.first.toInt() - 125).px
            }
        }

    val iframe: Iframe
    val urlInput: TextInput

    init {
        caption = "Web Browser - Due to security reasons navigation is limited to the same domain!"
        minWidth = 400.px
        minHeight = 150.px
        padding = 2.px
        iframe = Iframe("https://rjaros.github.io/kvision/kvision/index.html").apply {
            width = 100.perc
            height = 340.px
        }
        urlInput = TextInput().apply {
            width = 200.px
            marginLeft = 10.px
            setEventListener<TextInput> {
                change = {
                    this@WebBrowser.iframe.location = self.value
                }
            }
        }
        navbar {
            marginBottom = 0.px
            paddingLeft = 0.px
            navForm {
                paddingLeft = 0.px
                buttonGroup {
                    button("", icon = "fas fa-arrow-left").onClick {
                        this@WebBrowser.iframe.getIframeWindow()?.history?.back()
                    }
                    button("", icon = "fas fa-arrow-right").onClick {
                        this@WebBrowser.iframe.getIframeWindow()?.history?.forward()
                    }
                }
                add(this@WebBrowser.urlInput)
            }
        }
        add(iframe)
        iframe.setEventListener<Iframe> {
            load = {
                urlInput.value = iframe.location
            }
        }
        height = 457.px
    }

    companion object {
        fun run(container: Container) {
            container.add(WebBrowser())
        }
    }
}
