package com.example

import com.google.inject.AbstractModule
import net.jmob.guice.conf.core.BindConfig
import net.jmob.guice.conf.core.ConfigurationModule
import net.jmob.guice.conf.core.InjectConfig
import net.jmob.guice.conf.core.Syntax

@BindConfig(value = "application", syntax = Syntax.PROPERTIES)
class Config {
    @InjectConfig(value = "db.driver")
    lateinit var dbDriver: String

    @InjectConfig(value = "db.jdbcUrl")
    lateinit var dbJdbcUrl: String

    @InjectConfig(value = "db.username")
    var dbUsername: String? = null

    @InjectConfig(value = "db.password")
    var dbPassword: String? = null
}

class ConfigModule : AbstractModule() {
    override fun configure() {
        install(ConfigurationModule())
        requestInjection(Config::class.java)
        bind(Config::class.java)
    }
}
