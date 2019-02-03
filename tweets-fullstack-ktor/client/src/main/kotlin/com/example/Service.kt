package com.example

import pl.treksoft.kvision.remote.KVRemoteAgent

object TweetAgent : KVRemoteAgent<TweetService>(TweetServiceManager)

actual class TweetService : ITweetService {
    override suspend fun sendTweet(nickname: String, message: String, tags: List<String>) =
        TweetAgent.call(ITweetService::sendTweet, nickname, message, tags)

    override suspend fun getTweet(id: Int) = TweetAgent.call(ITweetService::getTweet, id)

    override suspend fun getTweets(limit: Int?) = TweetAgent.call(ITweetService::getTweets, limit)
}
