package com.example

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.GrantedAuthority
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
import pl.treksoft.e4k.core.DbClient
import io.kvision.remote.serviceMatchers
import org.springframework.data.annotation.Id
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

@Serializable
actual data class Profile(
    val id: String? = null,
    val name: String? = null
) : UserDetails {

    @Transient
    private var password: String? = null

    @Transient
    var password2: String? = null

    private var username: String? = null

    override fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    override fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }
}

@Table("users")
data class User(@Id val id: Int? = null, val username: String, val password: String, val name: String)

@Service
class MyReactiveUserDetailsService(private val client: DbClient) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return client.r2dbcEntityTemplate.select(User::class.java).matching(query(where("username").`is`(username)))
            .first().map {
            @Suppress("USELESS_CAST")
            Profile(it.id.toString(), it.name).apply {
                this.username = it.username
                this.password = it.password
            } as UserDetails
        }.switchIfEmpty(
            Mono.error(UsernameNotFoundException("User not found"))
        )
    }
}
