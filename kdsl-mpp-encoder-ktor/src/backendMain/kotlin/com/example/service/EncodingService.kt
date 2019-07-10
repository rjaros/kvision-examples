package com.example.service

import com.example.domain.EncodingType
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import javax.xml.bind.DatatypeConverter

actual class EncodingService : IEncodingService {
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

    override suspend fun decode(input: String, encodingType: EncodingType): String {
        return when (encodingType) {
            EncodingType.BASE64 -> {
                String(Base64.getDecoder().decode(input))
            }
            EncodingType.URLENCODE -> {
                URLDecoder.decode(input, "UTF-8")
            }
            EncodingType.HEX -> {
                String(DatatypeConverter.parseHexBinary(input))
            }
        }
    }
}