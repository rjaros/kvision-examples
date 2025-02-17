package com.example

import io.kvision.remote.KVServiceManager
import io.kvision.remote.serviceMatchers
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.relational.core.query.Criteria.where
import org.springframework.data.relational.core.query.Query.query
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.server.DefaultServerRedirectStrategy
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerRedirectStrategy
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import pl.treksoft.e4k.core.DbClient
import reactor.core.publisher.Mono
import java.net.URI

@EnableWebFluxSecurity
@Configuration
class SecurityConfiguration {

    //https://github.com/rjaros/kvision/issues/160
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity, serviceManagers : List<KVServiceManager<*>>,
                               successHandler: OAuth2LoginSuccessHandler): SecurityWebFilterChain {
        return http
            .authorizeExchange {
                serviceManagers.forEach { sm -> it.serviceMatchers(sm).authenticated().pathMatchers("/**").permitAll()  }

            }
            .csrf {
                it.disable()
            }
            .exceptionHandling {
                it.authenticationEntryPoint { exchange, _ ->
                    val response = exchange.response
                    response.statusCode = HttpStatus.UNAUTHORIZED
                    exchange.mutate().response(response)
                    Mono.empty()
                }
            }
            .oauth2Login{oauth2 ->
                oauth2.authenticationSuccessHandler(successHandler)
            }
            .logout {
                it.logoutUrl("/logout")
                    .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/logout"))
                    .logoutSuccessHandler(RedirectServerLogoutSuccessHandler().apply {
                        setLogoutSuccessUrl(URI.create("/"))
                    })
            }.build()
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
data class User(@Id val id: Int? = null, val username: String, val name: String)

@Service
class MyReactiveUserDetailsService(private val client: DbClient) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return client.r2dbcEntityTemplate.select(User::class.java).matching(query(where("username").`is`(username)))
            .first().map {
                @Suppress("USELESS_CAST")
                Profile(it.id.toString(), it.name).apply {
                    this.username = it.username
                } as UserDetails
            }.switchIfEmpty(
                Mono.error(UsernameNotFoundException("User not found"))
            )
    }
}

@Component
class OAuth2LoginSuccessHandler(private val client: DbClient) : ServerAuthenticationSuccessHandler {
    private val redirectStrategy: ServerRedirectStrategy = DefaultServerRedirectStrategy()

    override fun onAuthenticationSuccess(
        webFilterExchange: WebFilterExchange,
        authentication: Authentication?
    ): Mono<Void> {
        return if (authentication is OAuth2AuthenticationToken) {
            val oauth2User = authentication.principal as OAuth2User
            val attributes = oauth2User.attributes

            val email = attributes["email"] as String
            val name = attributes["name"] as String

            client.r2dbcEntityTemplate.select(User::class.java)
                .matching(query(where("username").`is`(email)))
                .first()
                .flatMap { existingUser ->
                    Mono.just(Profile(existingUser.id.toString(), existingUser.name).apply {
                        username = existingUser.username
                    } as UserDetails)
                }
                .switchIfEmpty(Mono.defer {
                    val newUser = User(username = email, name = name)
                    client.r2dbcEntityTemplate.insert(newUser)
                        .map { savedUser ->
                            Profile(savedUser.id.toString(), savedUser.name).apply {
                                username = savedUser.username
                            }
                        }
                })
                .flatMap {
                    val redirectUri = URI.create("http://localhost:3000")
                    redirectStrategy.sendRedirect(webFilterExchange.exchange, redirectUri)
                }
                .doOnError { throwable ->
                    println("Error in OAuth2 flow: " + throwable.message)
                    throwable.printStackTrace();
                }


        } else {
            Mono.error(IllegalStateException("Unsupported authentication type"))
        }
    }
}
