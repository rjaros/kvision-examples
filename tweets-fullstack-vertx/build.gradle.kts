import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    val kotlinVersion: String by System.getProperties()
    id("kotlinx-serialization") version kotlinVersion
    kotlin("multiplatform") version kotlinVersion
    val kvisionVersion: String by System.getProperties()
    id("kvision") version kvisionVersion
    val vertxPluginVersion: String by System.getProperties()
    id("io.vertx.vertx-plugin") version vertxPluginVersion
}

version = "1.0.0-SNAPSHOT"
group = "com.example"

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
    maven { url = uri("https://dl.bintray.com/rjaros/kotlin") }
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    mavenLocal()
}

// Versions
val kotlinVersion: String by System.getProperties()
val kvisionVersion: String by System.getProperties()
val logbackVersion: String by project

val webDir = file("src/frontendMain/web")

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
                sourceMaps = false
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
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("pl.treksoft:kvision-server-vertx:$kvisionVersion")
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
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
            }
        }
        val backendTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        val frontendMain by getting {
            resources.srcDir(webDir)
            dependencies {
                implementation("pl.treksoft:kvision:$kvisionVersion")
                implementation("pl.treksoft:kvision-bootstrap:$kvisionVersion")
                implementation("pl.treksoft:kvision-bootstrap-css:$kvisionVersion")
                implementation("pl.treksoft:kvision-fontawesome:$kvisionVersion")
                implementation("pl.treksoft:kvision-datacontainer:$kvisionVersion")
            }
            kotlin.srcDir("build/generated-src/frontend")
        }
        val frontendTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
                implementation("pl.treksoft:kvision-testutils:$kvisionVersion")
            }
        }
    }
}

fun getNodeJsBinaryExecutable(): String {
    val nodeDir = NodeJsRootPlugin.apply(project).nodeJsSetupTaskProvider.get().destination
    val isWindows = System.getProperty("os.name").toLowerCase().contains("windows")
    val nodeBinDir = if (isWindows) nodeDir else nodeDir.resolve("bin")
    val command = NodeJsRootPlugin.apply(project).nodeCommand
    val finalCommand = if (isWindows && command == "node") "node.exe" else command
    return nodeBinDir.resolve(finalCommand).absolutePath
}

vertx {
    mainVerticle = "com.example.MainVerticle"
    jvmArgs = listOf("-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory")
}

tasks {
    create("generatePotFile", Exec::class) {
        dependsOn("compileKotlinFrontend")
        executable = getNodeJsBinaryExecutable()
        args("$buildDir/js/node_modules/gettext-extract/bin/gettext-extract")
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
                            "$buildDir/js/node_modules/gettext.js/bin/po2json",
                            it.absolutePath,
                            "${it.parent}/${it.nameWithoutExtension}.json"
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
            into("/webroot")
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
        create("backendRun") {
            dependsOn("vertxRun")
            group = "run"
        }
        getByName("jar", Jar::class).apply {
            dependsOn("shadowJar")
        }
        getByName("backendJar").group = "package"
        getByName("shadowJar", ShadowJar::class) {
            archiveClassifier.set(null as String?)
            dependsOn("frontendArchive", "backendJar")
            val dependencies = project.configurations["backendRuntimeClasspath"].filter { it.name.endsWith(".jar") } +
                    project.tasks["backendJar"].outputs.files +
                    project.tasks["frontendArchive"].outputs.files
            dependencies.forEach {
                if (it.isDirectory) from(it) else from(zipTree(it))
            }
            exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
        getByName("compileKotlinBackend") {
            dependsOn("compileKotlinMetadata")
        }
        getByName("compileKotlinFrontend") {
            dependsOn("compileKotlinMetadata")
        }
    }
}
