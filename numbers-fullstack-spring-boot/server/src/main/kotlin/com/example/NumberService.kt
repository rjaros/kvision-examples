package com.example

import org.springframework.stereotype.Service
import pl.allegro.finance.tradukisto.ValueConverters

@Service
actual class NumberService : INumberService {

    override suspend fun numberToWords(number: Int, language: Language): String {
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
