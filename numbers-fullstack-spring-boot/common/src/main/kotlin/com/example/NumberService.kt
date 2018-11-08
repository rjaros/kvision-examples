package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.SpringServiceManager

interface INumberService {
    fun numberToWords(number: Int, language: Language): Deferred<String>
}

expect class NumberService : INumberService

object NumberServiceManager : SpringServiceManager<NumberService>(NumberService::class) {
    init {
        bind(INumberService::numberToWords)
    }
}
