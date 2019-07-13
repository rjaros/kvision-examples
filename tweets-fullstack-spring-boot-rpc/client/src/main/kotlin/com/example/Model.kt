package com.example

import pl.treksoft.kvision.utils.observableListOf


object Model {

    val tweetService = TweetService()

    val tweets = observableListOf<Tweet>()

}
