package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.SpringServiceManager

expect class NumberService() {
    fun numberToWords(number: Int, language: Language): Deferred<String>
}

object NumberServiceManager : SpringServiceManager<NumberService>(NumberService::class) {
    init {
        bind(NumberService::numberToWords)
    }
}
