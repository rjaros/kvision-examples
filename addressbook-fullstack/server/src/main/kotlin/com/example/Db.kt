package com.example

import com.github.andrewoma.kwery.core.dialect.Dialect
import com.github.andrewoma.kwery.core.dialect.HsqlDialect
import com.github.andrewoma.kwery.core.dialect.PostgresDialect
import com.typesafe.config.Config

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
