package com.example

import pl.treksoft.kvision.remote.SpringRemoteAgent

object NumberAgent : SpringRemoteAgent<NumberService>(NumberServiceManager)

actual class NumberService : INumberService {

    override fun numberToWords(number: Int, language: Language) =
        NumberAgent.call(INumberService::numberToWords, number, language)

}
