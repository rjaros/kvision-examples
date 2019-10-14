package com.example

import pl.treksoft.kvision.state.observableListOf


object Model {

    val tweetService = TweetService()

    val tweets = observableListOf<Tweet>()

}
