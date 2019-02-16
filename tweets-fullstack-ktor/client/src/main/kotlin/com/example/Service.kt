package com.example

import pl.treksoft.kvision.remote.KVRemoteAgent

actual class TweetService : ITweetService, KVRemoteAgent<TweetService>(TweetServiceManager) {
    override suspend fun sendTweet(nickname: String, message: String, tags: List<String>) =
        call(ITweetService::sendTweet, nickname, message, tags)

    override suspend fun getTweet(id: Int) = call(ITweetService::getTweet, id)

    override suspend fun getTweets(limit: Int?) = call(ITweetService::getTweets, limit)
}
