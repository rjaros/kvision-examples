@file:UseContextualSerialization(LocalDateTime::class)

package com.example

import io.kvision.annotations.KVService
import io.kvision.types.LocalDateTime
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization

@Serializable
data class Tweet(
    val date: LocalDateTime?,
    val nickname: String,
    val message: String,
    val tags: List<String>
)

@KVService
interface ITweetService {
    suspend fun socketConnection(input: ReceiveChannel<Tweet>, output: SendChannel<Tweet>) {}
}
