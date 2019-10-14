package com.example

import com.example.Model.clients
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

object Model {
    val clients = ConcurrentHashMap.newKeySet<SendChannel<Tweet>>()
}

actual class TweetService : ITweetService {

    override suspend fun socketConnection(input: ReceiveChannel<Tweet>, output: SendChannel<Tweet>) {
        clients.add(output)
        for (receivedTweet in input) {
            val tweet = receivedTweet.copy(date = LocalDateTime.now())
            clients.forEach {
                it.send(tweet)
            }
        }
        clients.remove(output)
    }

}
