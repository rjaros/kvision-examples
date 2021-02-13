package com.example

import io.kvision.state.observableListOf


object Model {

    val tweetService = TweetService()

    val tweets = observableListOf<Tweet>()

}
