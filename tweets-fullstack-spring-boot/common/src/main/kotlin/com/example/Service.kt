package com.example

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import pl.treksoft.kvision.remote.SpringServiceManager
import pl.treksoft.kvision.types.Date

@Serializable
data class Tweet(val id: Int, val date: Date, val nickname: String, val message: String, val tags: List<String>)

interface ITweetService {
    suspend fun sendTweet(nickname: String, message: String, tags: List<String>): Int
    suspend fun getTweet(id: Int): Tweet
    suspend fun getTweets(limit: Int? = null): List<Tweet>
}

expect class TweetService : ITweetService

object TweetServiceManager : SpringServiceManager<TweetService>(TweetService::class) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(ITweetService::sendTweet)
            bind(ITweetService::getTweet)
            bind(ITweetService::getTweets)
        }
    }
}
