package com.example

import io.jooby.Context
import org.pac4j.sql.profile.DbProfile

interface WithContext {
    val ctx: Context
}

suspend fun <RESP> WithContext.withProfile(block: suspend (Profile) -> RESP): RESP {
    return ctx.getUser<DbProfile>()?.let { dbProfile ->
        val profile =
            Profile(dbProfile.id, dbProfile.attributes.map { it.key to it.value.toString() }.toMap().toMutableMap())
        block(profile)
    } ?: throw IllegalStateException("Profile not set!")
}
