import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    val kotlinVersion: String by System.getProperties()
    id("kotlinx-serialization") version kotlinVersion
    kotlin("multiplatform") version kotlinVersion
    val joobyVersion: String by System.getProperties()
    id("io.jooby.run") version joobyVersion
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
    maven { url = uri("https://dl.bintray.com/rjaros/kotlin") }
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    mavenLocal()
}

// Versions
val kotlinVersion: String by System.getProperties()
val kvisionVersion: String by System.getProperties()
val joobyVersion: String by System.getProperties()
val h2Version: String by project
val pgsqlVersion: String by project
val kweryVersion: String by project
val logbackVersion: String by project
val commonsLoggingVersion: String by project
val pac4jVersion: String by project
val springSecurityCryptoVersion: String by project

val webDir = file("src/frontendMain/web")
val mainClassNameVal = "com.example.MainKt"

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
                        "/callback" to "http://localhost:8080",
                        "/logout" to "http://localhost:8080",
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
                api("pl.treksoft:kvision-server-jooby:$kvisionVersion")
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
                implementation("io.jooby:jooby-netty:$joobyVersion")
                implementation("io.jooby:jooby-hikari:$joobyVersion")
                implementation("io.jooby:jooby-pac4j:$joobyVersion")
                implementation("org.pac4j:pac4j-sql:$pac4jVersion")
                implementation("org.springframework.security:spring-security-crypto:$springSecurityCryptoVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("commons-logging:commons-logging:$commonsLoggingVersion")
                implementation("com.h2database:h2:$h2Version")
                implementation("org.postgresql:postgresql:$pgsqlVersion")
                implementation("com.github.andrewoma.kwery:core:$kweryVersion")
                implementation("com.github.andrewoma.kwery:mapper:$kweryVersion")
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
                implementation("pl.treksoft:kvision-bootstrap-select:$kvisionVersion")
                implementation("pl.treksoft:kvision-datacontainer:$kvisionVersion")
                implementation("pl.treksoft:kvision-bootstrap-dialog:$kvisionVersion")
                implementation("pl.treksoft:kvision-fontawesome:$kvisionVersion")
                implementation("pl.treksoft:kvision-i18n:$kvisionVersion")
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

tasks {
    joobyRun {
        mainClassName = mainClassNameVal
        restartExtensions = listOf("conf", "properties", "class")
        compileExtensions = listOf("java", "kt")
        port = 8080
    }
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
            into("/assets")
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
        getByName("backendJar").group = "package"
        create("shadowJar", Jar::class).apply {
            dependsOn("frontendArchive", "backendJar")
            group = "package"
            manifest {
                attributes(
                    mapOf(
                        "Implementation-Title" to rootProject.name,
                        "Implementation-Group" to rootProject.group,
                        "Implementation-Version" to rootProject.version,
                        "Timestamp" to System.currentTimeMillis(),
                        "Main-Class" to mainClassNameVal
                    )
                )
            }
            val dependencies = configurations["backendRuntimeClasspath"].filter { it.name.endsWith(".jar") } +
                    project.tasks["backendJar"].outputs.files +
                    project.tasks["frontendArchive"].outputs.files
            dependencies.forEach {
                if (it.isDirectory) from(it) else from(zipTree(it))
            }
            exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
            inputs.files(dependencies)
            outputs.file(archiveFile)
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
        getByName("jar", Jar::class).apply {
            dependsOn("shadowJar")
        }
        create("backendRun") {
            dependsOn("joobyRun")
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
