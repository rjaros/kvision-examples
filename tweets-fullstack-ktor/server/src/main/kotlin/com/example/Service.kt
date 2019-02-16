package com.example

import com.example.Model.counter
import com.example.Model.tweets
import java.util.*

object Model {
    val tweets = mutableListOf<Tweet>()
    var counter = 0
}

actual class TweetService : ITweetService {

    override suspend fun sendTweet(nickname: String, message: String, tags: List<String>): Int {
        synchronized(Model) {
            val tweet = Tweet(counter++, Date(), nickname, message, tags)
            tweets.add(tweet)
            return tweet.id
        }
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
