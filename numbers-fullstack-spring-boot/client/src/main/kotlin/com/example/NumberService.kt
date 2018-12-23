package com.example

import pl.treksoft.kvision.remote.KVRemoteAgent

object NumberAgent : KVRemoteAgent<NumberService>(NumberServiceManager)

actual class NumberService : INumberService {

    override suspend fun numberToWords(number: Int, language: Language) =
        NumberAgent.call(INumberService::numberToWords, number, language)

}
