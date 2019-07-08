import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.nodeJs

buildscript {
    extra.set("production", (findProperty("prod") ?: findProperty("production") ?: "false") == "true")
}
plugins {
    val kotlinVersion: String by System.getProperties()
    id("kotlin-multiplatform") version kotlinVersion
    id("kotlinx-serialization") version kotlinVersion
    kotlin("frontend") version System.getProperty("frontendPluginVersion")
    idea
}
group = "com.example"
version = "1.0-SNAPSHOT"

// Versions
val kotlinVersion: String by System.getProperties()
val ktorVersion: String by project
val logbackVersion: String by project
val kvisionVersion: String by project

// Custom Properties
val webDir = file("src/frontendMain/web")
val isProductionBuild = project.extra.get("production") as Boolean
val mainClassName = "io.ktor.server.netty.EngineMain"

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

repositories {
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    maven { url = uri("https://dl.bintray.com/gbaldeck/kotlin") }
    maven { url = uri("https://dl.bintray.com/rjaros/kotlin") }
    maven { url = uri("https://kotlin.bintray.com/kotlin-js-wrappers") }
    mavenLocal()
}

kotlin {
    jvm("backend") {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    js("frontend") {
        compilations.all {
            kotlinOptions {
                moduleKind = "umd"
                sourceMap = !isProductionBuild
                metaInfo = true
                if (!isProductionBuild) {
                    sourceMapEmbedSources = "always"
                }
            }
        }
    }
    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("pl.treksoft:kvision-common-types:$kvisionVersion")
                implementation("pl.treksoft:kvision-common-remote:$kvisionVersion")
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        getByName("backendMain") {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(kotlin("reflect"))
                implementation("pl.treksoft:kvision-server-ktor:$kvisionVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
            }
        }
        getByName("backendTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        getByName("frontendMain") {
            resources.srcDir(webDir)
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("pl.treksoft:kvision:$kvisionVersion")
                implementation("pl.treksoft:kvision-bootstrap:$kvisionVersion")
                implementation("pl.treksoft:kvision-select:$kvisionVersion")
                implementation("pl.treksoft:kvision-redux:$kvisionVersion")
                implementation("pl.treksoft:kvision-i18n:$kvisionVersion")
                implementation("pl.treksoft:kvision-remote:$kvisionVersion")
            }
        }
        getByName("frontendTest") {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

kotlinFrontend {
    downloadNodeJsVersion = "latest"
    sourceMaps = !isProductionBuild
    npm {
        devDependency("po2json")
        dependency("hammerjs")
        devDependency("workbox-webpack-plugin")
    }
    webpackBundle {
        bundleName = "main"
        sourceMapEnabled = false
        contentPath = file(webDir)
        port = 3000
        proxyUrl = "http://localhost:8080"
        mode = if (isProductionBuild) "production" else "development"
    }
    define("PRODUCTION", isProductionBuild)
}

afterEvaluate {
    tasks {
        getByName("backendJar").group = "jar"
        create("po2json") {
            dependsOn("npm-install")
            mustRunAfter("frontendProcessResources")
            val input = fileTree("src/frontendMain/resources/i18n").filter { it.extension == "po" }
            val outputDir = "${(project.tasks["frontendProcessResources"] as Copy).destinationDir}/i18n"
            inputs.files(input)
            outputs.dir(outputDir)
            doFirst {
                file(outputDir).mkdirs()
                input.forEach {
                    exec {
                        workingDir = file("$buildDir/node_modules")
                        executable = project.nodeJs.root.nodeCommand
                        args(".bin/po2json",
                                it.absolutePath,
                                "$outputDir/${it.nameWithoutExtension}.json",
                                "-f",
                                "jed1.x")
                        println("Converting ${it.name} to ${it.nameWithoutExtension}.json")
                        println(commandLine)
                    }
                }
            }
        }
        getByName("webpack-config") {
            dependsOn("frontendMainClasses")
            doFirst {
                file("${project.projectDir.resolve("webpack.config.d")}/resolve-modules.js").run {
                    if (!exists()) createNewFile()
                    writeText("config.resolve.modules.push('${(project.tasks["frontendProcessResources"] as Copy).destinationDir}');")
                }
            }
        }
        getByName("frontendProcessResources", Copy::class) {
            finalizedBy("po2json")
            exclude("i18n/*.po")
        }
        replace("frontendJar", Jar::class).apply {
            dependsOn("webpack-bundle")
            group = "jar"
            archiveAppendix.set("frontend")
            val sources = fileTree(webDir) + project.tasks["webpack-bundle"].outputs.files
            inputs.property("production", isProductionBuild)
            inputs.files(sources).skipWhenEmpty()

            into("/assets") {
                from(sources)
            }
        }
        replace("jar", Jar::class).apply {
            dependsOn("frontendJar", "backendJar")
            group = "jar"
            manifest {
                attributes(
                        mapOf(
                                "Implementation-Title" to rootProject.name,
                                "Implementation-Version" to rootProject.version,
                                "Timestamp" to System.currentTimeMillis(),
                                "Main-Class" to mainClassName
                        )
                )
            }
            val dependencies = configurations["backendRuntimeClasspath"].filter { it.name.endsWith(".jar") } +
                    project.tasks["backendJar"].outputs.files +
                    project.tasks["frontendJar"].outputs.files
            dependencies.forEach { from(zipTree(it)) }
        }
        create("frontendRun") {
            dependsOn("webpack-run")
            group = "run"
        }
        create("backendRun", JavaExec::class) {
            dependsOn("backendMainClasses")
            shouldRunAfter("frontendRun", "webpack-run")
            group = "run"
            main = mainClassName
            classpath += kotlin.targets["backend"].compilations["main"].output.allOutputs +
                    configurations["backendRuntimeClasspath"]
            isIgnoreExitValue = true
        }
        getByName("run") {
            dependsOn("frontendRun", "backendRun")
            finalizedBy("stop")
        }
    }
}

fun kotlinFrontend(block: KotlinFrontendExtension.() -> Unit) = configure(block)

fun KotlinFrontendExtension.webpackBundle(block: WebPackExtension.() -> Unit) =
        bundle("webpack", delegateClosureOf(block))

fun KotlinFrontendExtension.npm(block: NpmExtension.() -> Unit) = configure(block)

