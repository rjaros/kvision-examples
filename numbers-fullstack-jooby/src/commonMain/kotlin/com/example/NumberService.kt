package com.example

import io.kvision.annotations.KVService

@KVService
interface INumberService {
    suspend fun numberToWords(number: Int, language: Language): String
}
