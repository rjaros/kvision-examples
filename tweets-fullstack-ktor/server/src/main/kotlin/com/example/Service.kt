package com.example

import com.google.inject.Singleton
import java.util.*

@Singleton
actual class TweetService : ITweetService {

    private val tweets = mutableListOf<Tweet>()
    private var counter = 0

    override suspend fun sendTweet(nickname: String, message: String, tags: List<String>): Int {
        val tweet = Tweet(counter++, Date(), nickname, message, tags)
        tweets.add(tweet)
        return tweet.id
    }

    override suspend fun getTweet(id: Int): Tweet {
        return tweets.find { it.id == id } ?: throw Exception("Tweet not found")
    }

    override suspend fun getTweets(limit: Int?): List<Tweet> {
        return limit?.let { limit ->
            if (limit >= tweets.size) {
                tweets
            } else {
                tweets.drop(tweets.size - limit)
            }
        } ?: tweets
    }

}
