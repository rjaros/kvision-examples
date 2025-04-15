import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("plugin.serialization") version kotlinVersion
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    val kspVersion: String by System.getProperties()
    id("com.google.devtools.ksp") version kspVersion
    val kiluaRpcVersion: String by System.getProperties()
    id("dev.kilua.rpc") version kiluaRpcVersion
    val kvisionVersion: String by System.getProperties()
    id("io.kvision") version kvisionVersion
}

version = "1.0.0-SNAPSHOT"
group = "com.example"

// Versions
val kvisionVersion: String by System.getProperties()
val kiluaRpcVersion: String by System.getProperties()
val coroutinesVersion: String by project
val r2dbcPostgresqlVersion: String by project
val r2dbcH2Version: String by project
val e4kVersion: String by project

extra["mainClassName"] = "com.example.MainKt"

kotlin {
    jvmToolchain(21)
    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }
    js(IR) {
        browser {
            useEsModules()
            commonWebpackConfig {
                outputFileName = "main.bundle.js"
                sourceMaps = false
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
        binaries.executable()
        compilerOptions {
            target.set("es2015")
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("dev.kilua:kilua-rpc-spring-boot:$kiluaRpcVersion")
                implementation("io.kvision:kvision-common-remote:$kvisionVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("reflect"))
                implementation(project.dependencies.platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
                implementation("org.springframework.boot:spring-boot-starter")
                implementation("org.springframework.boot:spring-boot-starter-webflux")
                implementation("org.springframework.boot:spring-boot-starter-security")
                implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
                implementation("org.springframework.security:spring-security-oauth2-client")
                implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
                implementation("org.postgresql:r2dbc-postgresql:$r2dbcPostgresqlVersion")
                implementation("io.r2dbc:r2dbc-h2:$r2dbcH2Version")
                implementation("pl.treksoft:r2dbc-e4k:$e4kVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("io.kvision:kvision:$kvisionVersion")
                implementation("io.kvision:kvision-bootstrap:$kvisionVersion")
                implementation("io.kvision:kvision-state:$kvisionVersion")
                implementation("io.kvision:kvision-fontawesome:$kvisionVersion")
                implementation("io.kvision:kvision-i18n:$kvisionVersion")
                implementation("io.kvision:kvision-rest:$kvisionVersion")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                implementation("io.kvision:kvision-testutils:$kvisionVersion")
            }
        }
    }
}
