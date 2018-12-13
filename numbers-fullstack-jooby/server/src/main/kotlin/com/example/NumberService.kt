package com.example

import pl.allegro.finance.tradukisto.ValueConverters
import pl.treksoft.kvision.remote.Request

actual class NumberService : INumberService {

    override suspend fun numberToWords(number: Int, language: Language, req: Request?): String {
        val converter = when (language) {
            Language.ENGLISH -> ValueConverters.ENGLISH_INTEGER
            Language.GERMAN -> ValueConverters.GERMAN_INTEGER
            Language.RUSSIAN -> ValueConverters.RUSSIAN_INTEGER
            Language.POLISH -> ValueConverters.POLISH_INTEGER
            Language.CZECH -> ValueConverters.CZECH_INTEGER
        }
        return converter.asWords(number)
    }

}
