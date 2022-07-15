package com.example

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
import io.kvision.utils.syncWithList
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.events.KeyboardEvent

val AppScope = CoroutineScope(window.asCoroutineDispatcher())

class App : Application() {

    override fun start() {
        root("kvapp") {
            vPanel(JustifyContent.CENTER, AlignItems.CENTER, spacing = 5) {
                margin = 10.px
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
                                    val id = Model.tweetService.sendTweet(n, v, tagList)
                                    tweet.value = null
                                    tags.value = null
                                    val serverTweet = Model.tweetService.getTweet(id)
                                    if (!Model.tweets.contains(serverTweet)) Model.tweets.add(serverTweet)
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
        AppScope.launch {
            while (true) {
                val tweets = Model.tweetService.getTweets(20)
                Model.tweets.syncWithList(tweets)
                delay(5000)
            }
        }
    }
}

fun main() {
    startApplication(::App, module.hot, BootstrapModule, BootstrapCssModule, FontAwesomeModule, CoreModule)
}
