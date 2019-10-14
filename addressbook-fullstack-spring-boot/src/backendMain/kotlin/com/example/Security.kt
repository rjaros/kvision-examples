package com.example

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.data.relational.core.mapping.Table
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.stereotype.Service
import pl.treksoft.kvision.remote.Profile
import pl.treksoft.kvision.remote.serviceMatchers
import reactor.core.publisher.Mono
import java.net.URI

@EnableWebFluxSecurity
@Configuration
class SecurityConfiguration {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http.authorizeExchange()
            .serviceMatchers(AddressServiceManager, ProfileServiceManager).authenticated()
            .pathMatchers("/**").permitAll().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint { exchange, _ ->
                val response = exchange.response
                response.statusCode = HttpStatus.UNAUTHORIZED
                exchange.mutate().response(response)
                Mono.empty()
            }.and().formLogin().loginPage("/login")
            .authenticationSuccessHandler(RedirectServerAuthenticationSuccessHandler().apply {
                this.setRedirectStrategy { exchange, _ ->
                    Mono.fromRunnable {
                        val response = exchange.response
                        response.statusCode = HttpStatus.OK
                    }
                }
            }).authenticationFailureHandler(RedirectServerAuthenticationFailureHandler("/login").apply {
                this.setRedirectStrategy { exchange, _ ->
                    Mono.fromRunnable {
                        val response = exchange.response
                        response.statusCode = HttpStatus.UNAUTHORIZED
                    }
                }
            }).and().logout().logoutUrl("/logout")
            .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/logout"))
            .logoutSuccessHandler(RedirectServerLogoutSuccessHandler().apply {
                setLogoutSuccessUrl(URI.create("/"))
            }).and().build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}

@Table("users")
data class User(val id: Int? = null, val username: String, val password: String, val name: String)

@Service
class MyReactiveUserDetailsService(private val client: DatabaseClient) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return client.select().from(User::class.java).matching(where("username").`is`(username)).fetch().first().map {
            @Suppress("USELESS_CAST")
            Profile(it.id.toString()).apply {
                this.username = it.username
                this.password = it.password
                this.displayName = it.name
            } as UserDetails
        }.switchIfEmpty(
            Mono.error(UsernameNotFoundException("User not found"))
        )
    }
}
