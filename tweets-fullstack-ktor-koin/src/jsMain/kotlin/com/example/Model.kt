package com.example

import io.kvision.remote.getService
import io.kvision.state.observableListOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Model {

    val tweetService = getService<ITweetService>()

    val tweets = observableListOf<Tweet>()

    val tweetChannel = Channel<Tweet>()

    fun connectToServer() {
        AppScope.launch {
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
