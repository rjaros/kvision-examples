package com.example

import pl.treksoft.kvision.annotations.KVService

@KVService
interface INumberService {
    suspend fun numberToWords(number: Int, language: Language): String
}
