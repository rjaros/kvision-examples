package com.example

import pl.treksoft.kvision.annotations.KVService

enum class EncodingType {
    BASE64, URLENCODE, HEX
}

@KVService
interface IEncodingService {
    suspend fun encode(input: String, encodingType: EncodingType): String
}
