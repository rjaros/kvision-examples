package com.example

import kotlinx.coroutines.Deferred
import pl.treksoft.kvision.remote.SpringRemoteAgent

object NumberAgent : SpringRemoteAgent<NumberService>(NumberServiceManager)

actual class NumberService actual constructor() {

    actual fun numberToWords(number: Int, language: Language): Deferred<String> =
        NumberAgent.call(NumberService::numberToWords, number, language)

}
