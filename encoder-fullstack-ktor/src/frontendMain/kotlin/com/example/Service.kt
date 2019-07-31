package com.example

import pl.treksoft.kvision.remote.KVRemoteAgent

actual class EncodingService : IEncodingService, KVRemoteAgent<EncodingService>(EncodingServiceManager) {
    override suspend fun encode(input: String, encodingType: EncodingType) =
        call(IEncodingService::encode, input, encodingType)
}
