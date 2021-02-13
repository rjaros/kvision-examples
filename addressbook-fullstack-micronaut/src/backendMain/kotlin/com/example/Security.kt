package com.example

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.AuthenticationUserDetailsAdapter
import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.filters.SecurityFilter
import io.micronaut.security.handlers.LoginHandler
import io.micronaut.security.rules.AbstractSecurityRule
import io.micronaut.security.rules.SecurityRule
import io.micronaut.security.rules.SecurityRuleResult
import io.micronaut.security.session.SessionLoginHandler
import io.micronaut.security.token.RolesFinder
import io.micronaut.session.Session
import io.micronaut.session.SessionStore
import io.micronaut.session.http.SessionForRequest
import io.micronaut.web.router.RouteMatch
import org.reactivestreams.Publisher
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.relational.core.query.Criteria
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import io.kvision.remote.matches
import reactor.core.publisher.Mono
import reactor.kotlin.adapter.rxjava.toFlowable
import java.util.*
import javax.inject.Singleton

@Factory
open class PasswordEncoderFactory {
    @Singleton
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}

@Singleton
open class AppSecurityRule(rolesFinder: RolesFinder) : AbstractSecurityRule(rolesFinder) {
    override fun check(
        request: HttpRequest<*>,
        routeMatch: RouteMatch<*>?,
        claims: MutableMap<String, Any>?
    ): SecurityRuleResult {
        return if (request.matches(AddressServiceManager, ProfileServiceManager)) {
            compareRoles(listOf(SecurityRule.IS_AUTHENTICATED), getRoles(claims))
        } else {
            SecurityRuleResult.ALLOWED
        }
    }
}

@Replaces(SessionLoginHandler::class)
@Singleton
open class AppLoginHandler(private val sessionStore: SessionStore<Session>) : LoginHandler {
    override fun loginSuccess(userDetails: UserDetails?, request: HttpRequest<*>?): MutableHttpResponse<*> {
        val session = SessionForRequest.findOrCreate(request, sessionStore)
        session.put(SecurityFilter.AUTHENTICATION, AuthenticationUserDetailsAdapter(userDetails, "roles", "username"))
        return HttpResponse.ok<String>()
    }

    override fun loginRefresh(
        userDetails: UserDetails?,
        refreshToken: String?,
        request: HttpRequest<*>?
    ): MutableHttpResponse<*> {
        throw UnsupportedOperationException("Unsupported")
    }

    override fun loginFailed(
        authenticationResponse: AuthenticationResponse?,
        request: HttpRequest<*>?
    ): MutableHttpResponse<*> {
        return HttpResponse.unauthorized<String>()
    }
}

@Singleton
class AppAuthenticationProvider(
    private val databaseClient: DatabaseClient,
    private val passwordEncoder: PasswordEncoder
) :
    AuthenticationProvider {
    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        return databaseClient.select().from(User::class.java)
            .matching(
                Criteria.where("username").`is`(authenticationRequest.identity)
            ).fetch().first().flatMap {
                if (passwordEncoder.matches(authenticationRequest.secret.toString(), it.password)) {
                    Mono.just(
                        UserDetails(
                            authenticationRequest.identity as String,
                            ArrayList()
                        ) as AuthenticationResponse
                    )
                } else {
                    Mono.empty()
                }
            }.switchIfEmpty(Mono.error(AuthenticationException(AuthenticationFailed()))).toFlowable()
    }
}
