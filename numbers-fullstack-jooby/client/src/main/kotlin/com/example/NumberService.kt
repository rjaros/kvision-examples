package com.example

import pl.treksoft.kvision.remote.JoobyRemoteAgent
import pl.treksoft.kvision.remote.Request

object NumberAgent : JoobyRemoteAgent<NumberService>(NumberServiceManager)

actual class NumberService : INumberService {

    override suspend fun numberToWords(number: Int, language: Language, req: Request?) =
        NumberAgent.call(INumberService::numberToWords, number, language)

}
