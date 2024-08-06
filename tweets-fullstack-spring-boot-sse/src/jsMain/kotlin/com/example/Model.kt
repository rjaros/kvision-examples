package com.example

import io.kvision.remote.getService
import io.kvision.state.observableListOf
import kotlinx.coroutines.launch


object Model {

    val tweetService = getService<ITweetService>()

    val tweets = observableListOf<Tweet>()

    fun connectToServer() {
        AppScope.launch {
            tweetService.tweetsConnection { input ->
                for (tweet in input) {
                    tweets.add(tweet)
                }
            }
        }
    }
}
