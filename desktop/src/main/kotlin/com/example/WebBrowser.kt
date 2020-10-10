package com.example

import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.core.CssSize
import pl.treksoft.kvision.core.UNIT
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.Iframe
import pl.treksoft.kvision.navbar.navForm
import pl.treksoft.kvision.navbar.navbar
import pl.treksoft.kvision.toolbar.buttonGroup
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px

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
        iframe = Iframe("https://rjaros.github.io/kvision/api/").apply {
            width = 100.perc
            height = 340.px
        }
        urlInput = TextInput().apply {
            width = 200.px
            marginLeft = 10.px
            setEventListener<TextInput> {
                change = {
                    iframe.location = self.value
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
                        iframe.getIframeWindow().history.back()
                    }
                    button("", icon = "fas fa-arrow-right").onClick {
                        iframe.getIframeWindow().history.forward()
                    }
                }
                add(urlInput)
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
