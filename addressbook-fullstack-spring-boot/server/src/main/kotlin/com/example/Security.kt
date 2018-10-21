/*
 * Copyright (c) 2018. Robert Jaros
 */

package com.example

import org.pac4j.core.client.Clients
import org.pac4j.core.config.Config
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.http.client.indirect.FormClient
import org.pac4j.springframework.web.SecurityInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import pl.treksoft.kvision.remote.addPathPatternsFromServices

@Configuration
class Pac4jConfig {

    @Autowired
    lateinit var myDbProfileService: MyDbProfileService

    @Bean
    fun config(): Config {
        val formClient = FormClient("/") { credentials, context ->
            myDbProfileService.validate(credentials as UsernamePasswordCredentials, context)
        }
        val clients = Clients("http://localhost:8080/callback", formClient)
        val config = Config(clients)
        return config
    }

}

@Configuration
@ComponentScan(basePackages = arrayOf("org.pac4j.springframework.web"))
class SecurityConfig : WebMvcConfigurer {

    @Autowired
    private val config: Config? = null

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SecurityInterceptor(config, "FormClient"))
            .addPathPatternsFromServices(listOf(AddressServiceManager, ProfileServiceManager))
    }

}
