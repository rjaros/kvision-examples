package com.example

import org.pac4j.core.profile.CommonProfile
import pl.treksoft.kvision.remote.WithContext

actual typealias Profile = CommonProfile

suspend fun <RESP> WithContext.withProfile(block: suspend (Profile) -> RESP): RESP {
    return ctx.getUser<Profile>()?.let { profile ->
        block(profile)
    } ?: throw IllegalStateException("Profile not set!")
}
