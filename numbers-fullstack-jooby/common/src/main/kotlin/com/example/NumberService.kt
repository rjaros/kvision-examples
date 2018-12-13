package com.example

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.remote.JoobyServiceManager
import pl.treksoft.kvision.remote.Request

interface INumberService {
    suspend fun numberToWords(number: Int, language: Language, req: Request? = null): String
}

expect class NumberService() : INumberService

object NumberServiceManager : JoobyServiceManager<NumberService>(NumberService()) {
    init {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            bind(NumberService::numberToWords)
        }
    }
}
