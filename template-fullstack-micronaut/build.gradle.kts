import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("plugin.serialization") version kotlinVersion
    kotlin("multiplatform") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    val shadowVersion: String by System.getProperties()
    id("com.gradleup.shadow") version shadowVersion
    val kvisionVersion: String by System.getProperties()
    id("io.kvision") version kvisionVersion
    val micronautPluginsVersion: String by System.getProperties()
    id("io.micronaut.application") version micronautPluginsVersion
    id("io.micronaut.aot") version micronautPluginsVersion
}

version = "1.0.0-SNAPSHOT"
group = "com.example"

repositories {
    mavenCentral()
    mavenLocal()
}

// Versions
val kotlinVersion: String by System.getProperties()
val kvisionVersion: String by System.getProperties()
val micronautVersion: String by project
val coroutinesVersion: String by project

val mainClassNameVal = "com.example.MainKt"

application {
    mainClass.set(mainClassNameVal)
}

allOpen {
    annotation("io.micronaut.aop.Around")
}

kotlin {
    jvmToolchain(17)
    jvm {
        withJava()
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            javaParameters = true
        }
    }
    js(IR) {
        browser {
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
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("io.kvision:kvision-server-micronaut:$kvisionVersion")
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
                implementation("io.micronaut:micronaut-inject")
                implementation("io.micronaut.validation:micronaut-validation")
                implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
                implementation("io.micronaut:micronaut-runtime")
                implementation("io.micronaut:micronaut-http-server-netty")
                implementation("io.micronaut:micronaut-http-client")
                implementation("io.micronaut.session:micronaut-session")
                implementation("io.micronaut:micronaut-jackson-databind")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
                implementation("jakarta.validation:jakarta.validation-api")
                implementation("ch.qos.logback:logback-classic")
                implementation("org.yaml:snakeyaml")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("org.junit.jupiter:junit-jupiter-api")
                implementation("io.micronaut.test:micronaut-test-junit5")
                implementation("org.junit.jupiter:junit-jupiter-engine")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("io.kvision:kvision:$kvisionVersion")
                implementation("io.kvision:kvision-bootstrap:$kvisionVersion")
                implementation("io.kvision:kvision-i18n:$kvisionVersion")
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

micronaut {
    runtime("netty")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
    aot {
        optimizeServiceLoading.set(false)
        convertYamlToJava.set(false)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
    }
}

tasks {
    withType<JavaExec> {
        jvmArgs("-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")
        if (gradle.startParameter.isContinuous) {
            systemProperties(
                mapOf(
                    "micronaut.io.watch.restart" to "true",
                    "micronaut.io.watch.enabled" to "true",
                    "micronaut.io.watch.paths" to "src/jvmMain"
                )
            )
        }
    }
}

kapt {
    arguments {
        arg("micronaut.processing.incremental", true)
        arg("micronaut.processing.annotations", "com.example.*")
        arg("micronaut.processing.group", "com.example")
        arg("micronaut.processing.module", "template-fullstack-micronaut")
    }
}

dependencies {
    "kapt"(platform("io.micronaut.platform:micronaut-platform:$micronautVersion"))
    "kapt"("io.micronaut:micronaut-inject-java")
    "kapt"("io.micronaut.validation:micronaut-validation")
    "kaptTest"(platform("io.micronaut.platform:micronaut-platform:$micronautVersion"))
    "kaptTest"("io.micronaut:micronaut-inject-java")
}
