package com.example

import com.example.Model.clients
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

object Model {
    val clients = ConcurrentHashMap.newKeySet<SendChannel<Tweet>>()
}

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
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
