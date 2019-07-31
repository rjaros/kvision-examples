@file:ContextualSerialization(Date::class)

package com.example

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import pl.treksoft.kvision.remote.KVServiceManager
import pl.treksoft.kvision.types.Date

@Serializable
data class Tweet(
    val date: Date?,
    val nickname: String,
    val message: String,
    val tags: List<String>
)

interface ITweetService {
    suspend fun socketConnection(input: ReceiveChannel<Tweet>, output: SendChannel<Tweet>) {}
}

expect class TweetService : ITweetService

object TweetServiceManager : KVServiceManager<TweetService>(TweetService::class) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(ITweetService::socketConnection)
        }
    }
}
