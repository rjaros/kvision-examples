package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.RemoteAgent
import pl.treksoft.kvision.remote.Request

object NumberAgent : RemoteAgent<NumberService>(NumberServiceManager)

actual class NumberService actual constructor() {

    actual fun numberToWords(number: Int, language: Language, req: Request?): Deferred<String> =
        NumberAgent.call(NumberService::numberToWords, number, language)

}
