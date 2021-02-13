@file:UseContextualSerialization(LocalDateTime::class)

package com.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import io.kvision.annotations.KVService
import io.kvision.types.LocalDateTime

@Serializable
data class Tweet(
    val id: Int,
    val date: LocalDateTime,
    val nickname: String,
    val message: String,
    val tags: List<String>
)

@KVService
interface ITweetService {
    suspend fun sendTweet(nickname: String, message: String, tags: List<String>): Int
    suspend fun getTweet(id: Int): Tweet
    suspend fun getTweets(limit: Int? = null): List<Tweet>
}
