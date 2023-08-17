package com.example

import com.example.Model.clients
import com.example.Model.counter
import com.example.Model.tweets
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object Model {
    val clients = ConcurrentHashMap.newKeySet<SendChannel<Tweet>>()
    val tweets = mutableListOf<Tweet>()
    var counter = 0
}

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
actual class TweetService : ITweetService {

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun sendTweet(nickname: String, message: String, tags: List<String>): Int {
        val tweet = Tweet(counter++, LocalDateTime.now(), nickname, message, tags)
        tweets.add(tweet)
        clients.forEach {
            if (!it.isClosedForSend) {
                it.send(tweet)
            } else {
                clients.remove(it)
            }
        }
        return tweet.id
    }

    override suspend fun getTweet(id: Int): Tweet {
        return tweets.find { it.id == id } ?: throw Exception("Tweet not found")
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun tweetsConnection(output: SendChannel<Tweet>) {
        clients.add(output)
        while (!output.isClosedForSend) {
            delay(1000)
        }
    }

}
