@file:ContextualSerialization(LocalDateTime::class)

package com.example

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.serialization.ContextualSerialization
import kotlinx.serialization.Serializable
import pl.treksoft.kvision.annotations.KVService
import pl.treksoft.kvision.types.LocalDateTime

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
