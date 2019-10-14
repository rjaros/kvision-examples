package com.example

import com.example.Model.connectToServer
import com.example.Model.tweetChannel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.events.KeyboardEvent
import pl.treksoft.kvision.Application
import pl.treksoft.kvision.form.text.Text
import pl.treksoft.kvision.form.text.Text.Companion.text
import pl.treksoft.kvision.form.text.TextAreaInput.Companion.textAreaInput
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.Root.Companion.root
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.utils.ENTER_KEY
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px

class App : Application() {

    override fun start() {
        root("kvapp") {
            vPanel(FlexJustify.CENTER, FlexAlignItems.CENTER, spacing = 5) {
                margin = 10.px
                width = 100.perc
                val nickname = text(value = "Guest", label = "Nickname") {
                    width = 500.px
                }
                val tags = Text(label = "#")
                tags.width = 500.px
                hPanel(justify = FlexJustify.SPACEBETWEEN) {
                    width = 500.px
                    val tweet = textAreaInput {
                        width = 425.px
                        height = 120.px
                    }

                    val button = button("Post") {
                        width = 70.px
                        height = 100.perc
                    }

                    fun post() {
                        GlobalScope.launch {
                            val tagList = tags.value?.let {
                                it.split(" ").map { it.replace("#", "").replace(",", "").trim() }
                                    .filter { it.isNotBlank() }
                            } ?: listOf()
                            nickname.value?.let { n ->
                                tweet.value?.let { v ->
                                    tweetChannel.send(Tweet(null, n, v, tagList))
                                    tweet.value = null
                                    tags.value = null
                                    tweet.focus()
                                }
                            }
                        }
                    }

                    fun keyDownHandler(ev: KeyboardEvent) {
                        if (ev.ctrlKey && ev.keyCode == ENTER_KEY) {
                            post()
                        }
                    }

                    tweet.setEventListener {
                        keydown = ::keyDownHandler
                    }

                    tags.setEventListener {
                        keydown = ::keyDownHandler
                    }

                    button.onClick {
                        post()
                    }

                }
                add(tags)
                add(TweetPanel())
            }
        }
        connectToServer()
    }
}

fun main() {
    startApplication(::App)
}
