package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.Request
import pl.treksoft.kvision.remote.ServiceManager

expect class NumberService() {
    fun numberToWords(number: Int, language: Language, req: Request? = null): Deferred<String>
}

object NumberServiceManager : ServiceManager<NumberService>(NumberService()) {
    init {
        bind(NumberService::numberToWords)
    }
}
