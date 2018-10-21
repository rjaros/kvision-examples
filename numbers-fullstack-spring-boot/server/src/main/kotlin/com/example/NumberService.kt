package com.example

import kotlinx.coroutines.Deferred
import org.springframework.stereotype.Service
import pl.allegro.finance.tradukisto.ValueConverters
import pl.treksoft.kvision.remote.async

@Service
actual class NumberService actual constructor() {

    actual fun numberToWords(number: Int, language: Language): Deferred<String> = async {
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
