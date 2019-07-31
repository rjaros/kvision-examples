package com.example

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import pl.treksoft.kvision.remote.KVRemoteAgent

actual class TweetService : ITweetService, KVRemoteAgent<TweetService>(TweetServiceManager) {

    suspend fun socketConnection(handler: suspend (SendChannel<Tweet>, ReceiveChannel<Tweet>) -> Unit) =
        webSocket(ITweetService::socketConnection, handler)

}
