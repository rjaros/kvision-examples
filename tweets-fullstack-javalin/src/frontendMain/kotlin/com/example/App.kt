package com.example

import com.example.Model.connectToServer
import com.example.Model.tweetChannel
import io.kvision.Application
import io.kvision.BootstrapCssModule
import io.kvision.BootstrapModule
import io.kvision.CoreModule
import io.kvision.FontAwesomeModule
import io.kvision.core.AlignItems
import io.kvision.core.JustifyContent
import io.kvision.core.onEvent
import io.kvision.form.text.Text
import io.kvision.form.text.text
import io.kvision.form.text.textAreaInput
import io.kvision.html.button
import io.kvision.module
import io.kvision.panel.hPanel
import io.kvision.panel.root
import io.kvision.panel.vPanel
import io.kvision.startApplication
import io.kvision.utils.ENTER_KEY
import io.kvision.utils.perc
import io.kvision.utils.px
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.w3c.dom.events.KeyboardEvent

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

class App : Application() {

    override fun start() {
        root("kvapp") {
            vPanel(JustifyContent.CENTER, AlignItems.CENTER, spacing = 5) {
                margin = 10.px
                width = 100.perc
                val nickname = text(value = "Guest", label = "Nickname") {
                    width = 500.px
                }
                val tags = Text(label = "#")
                tags.width = 500.px
                hPanel(justify = JustifyContent.SPACEBETWEEN) {
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
                        AppScope.launch {
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

                    tweet.onEvent {
                        keydown = ::keyDownHandler
                    }

                    tags.onEvent {
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
    startApplication(::App, module.hot, BootstrapModule, BootstrapCssModule, FontAwesomeModule, CoreModule)
}
