package com.example

import com.github.andrewoma.kwery.core.dialect.Dialect
import com.github.andrewoma.kwery.core.dialect.HsqlDialect
import com.github.andrewoma.kwery.core.dialect.PostgresDialect
import com.typesafe.config.Config
import org.pac4j.core.credentials.password.SpringSecurityPasswordEncoder
import org.pac4j.sql.profile.service.DbProfileService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.inject.Inject
import javax.inject.Named
import javax.sql.DataSource

class MyDbProfileService @Inject constructor(@Named("db") ds: DataSource) :
    DbProfileService(ds, SpringSecurityPasswordEncoder(BCryptPasswordEncoder()))

fun getDbDialect(config: Config): Dialect {
    val dbdialect = if (config.hasPath("dbdialect")) {
        config.getString("dbdialect")
    } else {
        "hsql"
    }
    return when (dbdialect) {
        "pgsql" -> PostgresDialect()
        else -> HsqlDialect()
    }
}
