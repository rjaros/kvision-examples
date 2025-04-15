package com.example

import dev.kilua.rpc.annotations.RpcService
import kotlinx.serialization.Serializable

@Serializable
enum class EncodingType {
    BASE64, URLENCODE, HEX
}

@RpcService
interface IEncodingService {
    suspend fun encode(input: String, encodingType: EncodingType): String
}
