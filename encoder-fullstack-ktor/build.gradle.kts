import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("plugin.serialization") version kotlinVersion
    kotlin("multiplatform") version kotlinVersion
    val kspVersion: String by System.getProperties()
    id("com.google.devtools.ksp") version kspVersion
    val kiluaRpcVersion: String by System.getProperties()
    id("dev.kilua.rpc") version kiluaRpcVersion
    val kvisionVersion: String by System.getProperties()
    id("io.kvision") version kvisionVersion
}

version = "1.0.0-SNAPSHOT"
group = "com.example"

repositories {
    mavenCentral()
    mavenLocal()
}

// Versions
val kvisionVersion: String by System.getProperties()
val kiluaRpcVersion: String by System.getProperties()
val ktorVersion = project.property("ktorVersion") as String
val logbackVersion = project.property("logbackVersion") as String

val mainClassName = "io.ktor.server.netty.EngineMain"

kotlin {
    jvmToolchain(25)
    jvm {
        compilerOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass.set(mainClassName)
        }
    }
    js {
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
        getByName("commonMain") {
            dependencies {
                implementation("dev.kilua:kilua-rpc-ktor:$kiluaRpcVersion")
                implementation("io.kvision:kvision-common-remote:$kvisionVersion")
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        getByName("jvmMain") {
            dependencies {
                implementation(kotlin("reflect"))
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("io.ktor:ktor-server-compression:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")
            }
        }
        getByName("jvmTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        getByName("jsMain") {
            dependencies {
                implementation("io.kvision:kvision:$kvisionVersion")
                implementation("io.kvision:kvision-bootstrap:$kvisionVersion")
                implementation("io.kvision:kvision-tom-select:$kvisionVersion")
                implementation("io.kvision:kvision-i18n:$kvisionVersion")
                implementation("io.kvision:kvision-fontawesome:$kvisionVersion")
            }
        }
        getByName("jsTest") {
            dependencies {
                implementation(kotlin("test-js"))
                implementation("io.kvision:kvision-testutils:$kvisionVersion")
            }
        }
    }
}
