package com.example

import io.kvision.annotations.KVService

enum class EncodingType {
    BASE64, URLENCODE, HEX
}

@KVService
interface IEncodingService {
    suspend fun encode(input: String, encodingType: EncodingType): String
}
