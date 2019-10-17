package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.events.KeyboardEvent
import pl.treksoft.kvision.Application
import pl.treksoft.kvision.form.text.Text
import pl.treksoft.kvision.form.text.text
import pl.treksoft.kvision.form.text.textAreaInput
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.utils.ENTER_KEY
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px
import pl.treksoft.kvision.utils.syncWithList

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
        GlobalScope.launch {
            while (true) {
                val tweets = Model.tweetService.getTweets(20)
                Model.tweets.syncWithList(tweets)
                delay(5000)
            }
        }
    }
}

fun main() {
    startApplication(::App)
}
