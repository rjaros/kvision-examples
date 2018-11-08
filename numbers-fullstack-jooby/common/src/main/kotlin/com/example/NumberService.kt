package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.JoobyServiceManager
import pl.treksoft.kvision.remote.Request

interface INumberService {
    fun numberToWords(number: Int, language: Language, req: Request? = null): Deferred<String>
}

expect class NumberService() : INumberService

object NumberServiceManager : JoobyServiceManager<NumberService>(NumberService()) {
    init {
        bind(NumberService::numberToWords)
    }
}
