package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.JoobyServiceManager
import pl.treksoft.kvision.remote.Request

expect class NumberService() {
    fun numberToWords(number: Int, language: Language, req: Request? = null): Deferred<String>
}

object NumberServiceManager : JoobyServiceManager<NumberService>(NumberService()) {
    init {
        bind(NumberService::numberToWords)
    }
}
