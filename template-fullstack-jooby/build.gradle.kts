import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.util.nodePath
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackRunTask
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsExec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.nodeJs
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJsDce

buildscript {
    extra.set("production", (findProperty("prod") ?: findProperty("production") ?: "false") == "true")
}

plugins {
    val kotlinVersion: String by System.getProperties()
    id("kotlinx-serialization") version kotlinVersion
    id("kotlin-multiplatform") version kotlinVersion
    id("kotlin-dce-js") version kotlinVersion
    kotlin("frontend") version System.getProperty("frontendPluginVersion")
}

version = "1.0.0-SNAPSHOT"
group = "com.example"

repositories {
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    maven { url = uri("https://dl.bintray.com/gbaldeck/kotlin") }
    maven { url = uri("https://dl.bintray.com/rjaros/kotlin") }
    mavenLocal()
}

// Versions
val kotlinVersion: String by System.getProperties()
val joobyVersion: String by project
val pac4jVersion: String by project
val springSecurityCryptoVersion: String by project
val h2Version: String by project
val pgsqlVersion: String by project
val kweryVersion: String by project
val commonsLoggingVersion: String by project
val kvisionVersion: String by project

// Custom Properties
val webDir = file("src/frontendMain/web")
val isProductionBuild = project.extra.get("production") as Boolean
val mainClassName = "com.example.MainKt"

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
                implementation("pl.treksoft:kvision-server-jooby:$kvisionVersion")
                implementation("org.jooby:jooby-netty:$joobyVersion")
                implementation("org.jooby:jooby-jdbc:$joobyVersion")
                implementation("org.jooby:jooby-pac4j2:$joobyVersion")
                implementation("org.pac4j:pac4j-sql:$pac4jVersion")
                implementation("org.springframework.security:spring-security-crypto:$springSecurityCryptoVersion")
                implementation("commons-logging:commons-logging:$commonsLoggingVersion")
                implementation("com.h2database:h2:$h2Version")
                implementation("org.postgresql:postgresql:$pgsqlVersion")
                implementation("com.github.andrewoma.kwery:core:$kweryVersion")
                implementation("com.github.andrewoma.kwery:mapper:$kweryVersion")
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
                implementation("pl.treksoft:kvision-datacontainer:$kvisionVersion")
                implementation("pl.treksoft:kvision-dialog:$kvisionVersion")
                implementation("pl.treksoft:kvision-remote:$kvisionVersion")
                implementation("pl.treksoft:kvision-i18n:$kvisionVersion")
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
    sourceMaps = !isProductionBuild
    npm {
        devDependency("po2json")
        devDependency("grunt")
        devDependency("grunt-pot")
    }
    webpackBundle {
        bundleName = "main"
        sourceMapEnabled = false
        port = 3000
        proxyUrl = "http://localhost:8080"
        contentPath = webDir
        mode = if (isProductionBuild) "production" else "development"
    }

    define("PRODUCTION", isProductionBuild)
}

tasks {
    withType<Kotlin2JsCompile> {
        kotlinOptions {
            moduleKind = "umd"
            sourceMap = !isProductionBuild
            metaInfo = true
            if (!isProductionBuild) {
                sourceMapEmbedSources = "always"
            }
        }
    }
    withType<KotlinJsDce> {
        dceOptions {
            devMode = !isProductionBuild
        }
        doLast {
            copy {
                file("$buildDir/node_modules_imported/").listFiles()?.forEach {
                    if (it.isDirectory && it.name.startsWith("kvision")) {
                        from(it) {
                            include("css/**")
                            include("img/**")
                            include("js/**")
                        }
                    }
                }
                into(file(buildDir.path + "/kotlin-js-min/frontend/main"))
            }
        }
    }
    create("generateNpmScripts") {
        doFirst("generatePotScript") {
            file("$projectDir/package.json.d/pot.json").run {
                parentFile.mkdirs()
                writeText("""{"scripts": {"pot": "grunt pot"}}""")
            }
        }
    }
    create("generateGruntfile") {
        outputs.file("$buildDir/Gruntfile.js")
        doLast {
            file("$buildDir/Gruntfile.js").run {
                writeText("""
                    module.exports = function (grunt) {
                        grunt.initConfig({
                            pot: {
                                options: {
                                    text_domain: "messages",
                                    dest: "$buildDir/processedResources/frontend/main/i18n/",
                                    keywords: ["tr", "ntr:1,2", "gettext", "ngettext:1,2"],
                                    encoding: "UTF-8"
                                },
                                files: {
                                    src: ["$projectDir/src/frontendMain/kotlin/**/*.kt"],
                                    expand: true,
                                },
                            }
                        });
                        grunt.loadNpmTasks("grunt-pot");
                    };
                """.trimIndent())
            }
        }
    }
}
afterEvaluate {
    tasks {
        create("generatePotFile", NodeJsExec::class) {
            dependsOn("npm-install", "generateNpmScripts", "generateGruntfile", "frontendProcessResources")
            workingDir = file("$buildDir")
            args(nodePath(project, "npm").first().absolutePath, "run", "pot")
        }
        getByName("frontendProcessResources", Copy::class) {
            dependsOn("npm-install")
            doLast("Convert PO to JSON") {
                destinationDir.walkTopDown().filter {
                    it.isFile && it.extension == "po"
                }.forEach {
                    exec {
                        executable = project.nodeJs.root.nodeCommand
                        args("$buildDir/node_modules/.bin/po2json",
                                it.absolutePath,
                                "${it.parent}/${it.nameWithoutExtension}.json",
                                "-f",
                                "jed1.x")
                        println("Converted ${it.name} to ${it.nameWithoutExtension}.json")
                    }
                    it.delete()
                }
            }
        }
        getByName("npm-configure").shouldRunAfter("generateNpmScripts")
        getByName("webpack-run", WebPackRunTask::class) {
            dependsOn("frontendMainClasses")
            doFirst {
                copy {
                    from((project.tasks["frontendProcessResources"] as Copy).destinationDir)
                    into((project.tasks["processResources"] as Copy).destinationDir)
                }
            }
        }
        getByName("webpack-bundle") {
            dependsOn("frontendMainClasses", "runDceFrontendKotlin")
            doFirst {
                copy {
                    from((project.tasks["frontendProcessResources"] as Copy).destinationDir)
                    into((project.tasks["processResources"] as Copy).destinationDir)
                }
            }
        }
        replace("frontendJar", Jar::class).apply {
            dependsOn("webpack-bundle")
            group = "package"
            archiveAppendix.set("frontend")
            val from = project.tasks["webpack-bundle"].outputs.files + webDir
            from(from)
            into("/assets")
            inputs.files(from)
            outputs.file(archiveFile)

            manifest {
                attributes(
                        mapOf(
                                "Implementation-Title" to rootProject.name,
                                "Implementation-Group" to rootProject.group,
                                "Implementation-Version" to rootProject.version,
                                "Timestamp" to System.currentTimeMillis()
                        )
                )
            }
        }
        create("frontendZip", Zip::class) {
            dependsOn("webpack-bundle")
            group = "package"
            archiveAppendix.set("frontend")
            destinationDirectory.set(file("$buildDir/libs"))
            val from = project.tasks["webpack-bundle"].outputs.files + webDir
            from(from)
            inputs.files(from)
            outputs.file(archiveFile)
        }
        getByName("backendJar").group = "package"
        replace("jar", Jar::class).apply {
            dependsOn("frontendJar", "backendJar")
            group = "package"
            manifest {
                attributes(
                        mapOf(
                                "Implementation-Title" to rootProject.name,
                                "Implementation-Group" to rootProject.group,
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
            inputs.files(dependencies)
            outputs.file(archiveFile)
        }
        create("frontendRun") {
            dependsOn("webpack-run")
            group = "run"
        }
        create("backendRun", JavaExec::class) {
            dependsOn("compileKotlinBackend")
            shouldRunAfter("frontendRun", "webpack-run")
            group = "run"
            main = mainClassName
            classpath = configurations["backendRuntimeClasspath"] + project.tasks["compileKotlinBackend"].outputs.files +
                    project.tasks["backendProcessResources"].outputs.files
            doFirst {
                println(project.tasks["backendProcessResources"].outputs.files)
            }
        }
        getByName("run") {
            dependsOn("frontendRun", "backendRun")
        }
        create("frontendStop") {
            dependsOn("webpack-stop")
            group = "run"
        }
        getByName("stop") {
            dependsOn("frontendStop")
        }
    }
}

fun KotlinFrontendExtension.webpackBundle(block: WebPackExtension.() -> Unit) =
        bundle("webpack", delegateClosureOf(block))

fun KotlinFrontendExtension.npm(block: NpmExtension.() -> Unit) = configure(block)