package com.example

import java.net.URLEncoder
import java.util.*
import javax.xml.bind.DatatypeConverter

class EncodingService : IEncodingService {
    override suspend fun encode(input: String, encodingType: EncodingType): String {
        return when (encodingType) {
            EncodingType.BASE64 -> {
                Base64.getEncoder().encodeToString(input.toByteArray())
            }
            EncodingType.URLENCODE -> {
                URLEncoder.encode(input, "UTF-8")
            }
            EncodingType.HEX -> {
                DatatypeConverter.printHexBinary(input.toByteArray())
            }
        }
    }
}
