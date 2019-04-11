package com.example

import com.lightningkite.kotlin.observable.list.observableListOf

object Model {

    val tweetService = TweetService()

    val tweets = observableListOf<Tweet>()

}
