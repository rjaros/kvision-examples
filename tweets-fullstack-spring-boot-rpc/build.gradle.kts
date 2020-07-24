import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    val kotlinVersion: String by System.getProperties()
    id("kotlinx-serialization") version kotlinVersion
    kotlin("multiplatform") version kotlinVersion
    id("io.spring.dependency-management") version System.getProperty("dependencyManagementPluginVersion")
    id("org.springframework.boot") version System.getProperty("springBootVersion")
    kotlin("plugin.spring") version kotlinVersion
    val kvisionVersion: String by System.getProperties()
    id("kvision") version kvisionVersion
}

version = "1.0.0-SNAPSHOT"
group = "com.example"

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
    maven {
        url = uri("https://dl.bintray.com/gbaldeck/kotlin")
        metadataSources {
            mavenPom()
            artifact()
        }
    }
    maven { url = uri("https://dl.bintray.com/rjaros/kotlin") }
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    mavenLocal()
}

// Versions
val kotlinVersion: String by System.getProperties()
val kvisionVersion: String by System.getProperties()
val coroutinesVersion: String by project
val springAutoconfigureR2dbcVersion: String by project
val springDataR2dbcVersion: String by project
val r2dbcPostgresqlVersion: String by project
val r2dbcH2Version: String by project
val kweryVersion: String by project

val webDir = file("src/frontendMain/web")
val mainClassName = "com.example.MainKt"

kotlin {
    jvm("backend") {
        withJava()
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = listOf("-Xjsr305=strict")
            }
        }
    }
    js("frontend") {
        browser {
            runTask {
                outputFileName = "main.bundle.js"
                devServer = KotlinWebpackConfig.DevServer(
                    open = false,
                    port = 3000,
                    proxy = mapOf(
                        "/kv/*" to "http://localhost:8080",
                        "/kvws/*" to mapOf("target" to "ws://localhost:8080", "ws" to true)
                    ),
                    contentBase = listOf("$buildDir/processedResources/frontend/main")
                )
            }
            webpackTask {
                outputFileName = "main.bundle.js"
            }
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api("pl.treksoft:kvision-server-spring-boot:$kvisionVersion")
            }
            kotlin.srcDir("build/generated-src/common")
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val backendMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(kotlin("reflect"))
                implementation("org.springframework.boot:spring-boot-starter")
                implementation("org.springframework.boot:spring-boot-devtools")
                implementation("org.springframework.boot:spring-boot-starter-webflux")
            }
        }
        val backendTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }
        val frontendMain by getting {
            resources.srcDir(webDir)
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation(npm("po2json"))
                implementation(npm("grunt"))
                implementation(npm("grunt-pot"))

                implementation("pl.treksoft:kvision:$kvisionVersion")
                implementation("pl.treksoft:kvision-bootstrap:$kvisionVersion")
                implementation("pl.treksoft:kvision-bootstrap-css:$kvisionVersion")
                implementation("pl.treksoft:kvision-datacontainer:$kvisionVersion")
            }
            kotlin.srcDir("build/generated-src/frontend")
        }
        val frontendTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                implementation("pl.treksoft:kvision-testutils:$kvisionVersion:tests")
            }
        }
    }
}

fun getNodeJsBinaryExecutable(): String {
    val nodeDir = NodeJsRootPlugin.apply(project).nodeJsSetupTask.destination
    val isWindows = System.getProperty("os.name").toLowerCase().contains("windows")
    val nodeBinDir = if (isWindows) nodeDir else nodeDir.resolve("bin")
    val command = NodeJsRootPlugin.apply(project).nodeCommand
    val finalCommand = if (isWindows && command == "node") "node.exe" else command
    return nodeBinDir.resolve(finalCommand).absolutePath
}

tasks {
    create("generateGruntfile") {
        outputs.file("$buildDir/js/Gruntfile.js")
        doLast {
            file("$buildDir/js/Gruntfile.js").run {
                writeText(
                    """
                    module.exports = function (grunt) {
                        grunt.initConfig({
                            pot: {
                                options: {
                                    text_domain: "messages",
                                    dest: "../../src/frontendMain/resources/i18n/",
                                    keywords: ["tr", "ntr:1,2", "gettext", "ngettext:1,2"],
                                    encoding: "UTF-8"
                                },
                                files: {
                                    src: ["../../src/frontendMain/kotlin/**/*.kt"],
                                    expand: true,
                                },
                            }
                        });
                        grunt.loadNpmTasks("grunt-pot");
                    };
                """.trimIndent()
                )
            }
        }
    }
    create("generatePotFile", Exec::class) {
        dependsOn("compileKotlinFrontend", "generateGruntfile")
        workingDir = file("$buildDir/js")
        executable = getNodeJsBinaryExecutable()
        args("$buildDir/js/node_modules/grunt/bin/grunt", "pot")
        inputs.files(kotlin.sourceSets["frontendMain"].kotlin.files)
        outputs.file("$projectDir/src/frontendMain/resources/i18n/messages.pot")
    }
}
afterEvaluate {
    tasks {
        getByName("frontendProcessResources", Copy::class) {
            dependsOn("compileKotlinFrontend")
            exclude("**/*.pot")
            doLast("Convert PO to JSON") {
                destinationDir.walkTopDown().filter {
                    it.isFile && it.extension == "po"
                }.forEach {
                    exec {
                        executable = getNodeJsBinaryExecutable()
                        args(
                            "$buildDir/js/node_modules/po2json/bin/po2json",
                            it.absolutePath,
                            "${it.parent}/${it.nameWithoutExtension}.json",
                            "-f",
                            "jed1.x"
                        )
                        println("Converted ${it.name} to ${it.nameWithoutExtension}.json")
                    }
                    it.delete()
                }
            }
        }
        create("frontendArchive", Jar::class).apply {
            dependsOn("frontendBrowserProductionWebpack")
            group = "package"
            archiveAppendix.set("frontend")
            val distribution =
                project.tasks.getByName("frontendBrowserProductionWebpack", KotlinWebpack::class).destinationDirectory!!
            from(distribution) {
                include("*.*")
            }
            from(webDir)
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            into("/public")
            inputs.files(distribution, webDir)
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
        getByName("backendProcessResources", Copy::class) {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
        getByName("bootJar", BootJar::class) {
            dependsOn("frontendArchive", "backendMainClasses")
            classpath = files(
                kotlin.targets["backend"].compilations["main"].output.allOutputs +
                        project.configurations["backendRuntimeClasspath"] +
                        (project.tasks["frontendArchive"] as Jar).archiveFile
            )
        }
        getByName("jar", Jar::class).apply {
            dependsOn("bootJar")
        }
        getByName("bootRun", BootRun::class) {
            dependsOn("backendMainClasses")
            classpath = files(
                kotlin.targets["backend"].compilations["main"].output.allOutputs +
                        project.configurations["backendRuntimeClasspath"]
            )
        }
        create("backendRun") {
            dependsOn("bootRun")
            group = "run"
        }
        getByName("compileKotlinBackend") {
            dependsOn("compileKotlinMetadata")
        }
        getByName("compileKotlinFrontend") {
            dependsOn("compileKotlinMetadata")
        }
    }
}
