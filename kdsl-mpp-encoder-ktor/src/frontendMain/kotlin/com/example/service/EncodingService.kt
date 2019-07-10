package com.example.service

import com.example.domain.EncodingType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pl.treksoft.kvision.remote.KVRemoteAgent

@ExperimentalCoroutinesApi
actual class EncodingService : IEncodingService, KVRemoteAgent<EncodingService>(EncodingServiceManager) {
    override suspend fun encode(input: String, encodingType: EncodingType) =
            call(IEncodingService::encode, input, encodingType)

    override suspend fun decode(input: String, encodingType: EncodingType) =
            call(IEncodingService::decode, input, encodingType)
}