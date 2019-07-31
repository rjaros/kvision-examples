package com.example

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.remote.KVServiceManager

enum class EncodingType {
    BASE64, URLENCODE, HEX
}

interface IEncodingService {
    suspend fun encode(input: String, encodingType: EncodingType): String
}

expect class EncodingService : IEncodingService

object EncodingServiceManager : KVServiceManager<EncodingService>(EncodingService::class) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(IEncodingService::encode)
        }
    }
}
