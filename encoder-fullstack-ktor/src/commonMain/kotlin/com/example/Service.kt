package com.example

import io.kvision.annotations.KVService
import kotlinx.serialization.Serializable

@Serializable
enum class EncodingType {
    BASE64, URLENCODE, HEX
}

@KVService
interface IEncodingService {
    suspend fun encode(input: String, encodingType: EncodingType): String
}
