package com.example

import pl.treksoft.kvision.annotations.KVService

@KVService
interface IPingService {
    suspend fun ping(message: String): String
}
