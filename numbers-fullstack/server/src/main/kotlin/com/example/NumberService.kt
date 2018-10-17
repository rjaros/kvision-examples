package com.example

import kotlinx.coroutines.Deferred
import pl.allegro.finance.tradukisto.ValueConverters
import pl.treksoft.kvision.remote.Request
import pl.treksoft.kvision.remote.async

actual class NumberService actual constructor() {

    actual fun numberToWords(number: Int, language: Language, req: Request?): Deferred<String> =
        req.async { _, _, _ ->
            val converter = when (language) {
                Language.ENGLISH -> ValueConverters.ENGLISH_INTEGER
                Language.GERMAN -> ValueConverters.GERMAN_INTEGER
                Language.RUSSIAN -> ValueConverters.RUSSIAN_INTEGER
                Language.POLISH -> ValueConverters.POLISH_INTEGER
                Language.CZECH -> ValueConverters.CZECH_INTEGER
            }
            converter.asWords(number)
        }

}
