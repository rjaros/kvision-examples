package com.example

import pl.treksoft.kvision.remote.KVRemoteAgent

actual class NumberService : INumberService, KVRemoteAgent<NumberService>(NumberServiceManager) {

    override suspend fun numberToWords(number: Int, language: Language) =
        call(INumberService::numberToWords, number, language)

}
