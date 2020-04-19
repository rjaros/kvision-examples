package com.example

import io.jooby.Context
import org.pac4j.core.profile.CommonProfile

actual typealias Profile = CommonProfile

interface WithContext {
    val ctx: Context
}

suspend fun <RESP> WithContext.withProfile(block: suspend (Profile) -> RESP): RESP {
    return ctx.getUser<Profile>()?.let { profile ->
        block(profile)
    } ?: throw IllegalStateException("Profile not set!")
}
