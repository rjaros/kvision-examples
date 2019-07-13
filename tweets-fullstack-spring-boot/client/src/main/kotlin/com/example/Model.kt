package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.treksoft.kvision.utils.observableListOf

object Model {

    val tweetService = TweetService()

    val tweets = observableListOf<Tweet>()

    val tweetChannel = Channel<Tweet>()

    fun connectToServer() {
        GlobalScope.launch {
            while (true) {
                tweetService.socketConnection { output, input ->
                    coroutineScope {
                        launch {
                            for (tweet in tweetChannel) {
                                output.send(tweet)
                            }
                        }
                        launch {
                            for (tweet in input) {
                                tweets.add(tweet)
                            }
                        }
                    }
                }
                delay(5000)
            }
        }
    }
}
