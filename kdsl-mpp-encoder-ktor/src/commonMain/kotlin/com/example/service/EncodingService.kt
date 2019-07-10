package com.example.service

import com.example.domain.EncodingType
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.remote.KVServiceManager

interface IEncodingService {
    suspend fun encode(input: String, encodingType: EncodingType): String
    suspend fun decode(input: String, encodingType: EncodingType): String
}

expect class EncodingService : IEncodingService

@ExperimentalCoroutinesApi
object EncodingServiceManager : KVServiceManager<EncodingService>(EncodingService::class) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(IEncodingService::encode)
            bind(IEncodingService::decode)
        }
    }
}