import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("plugin.serialization") version kotlinVersion
    kotlin("multiplatform") version kotlinVersion
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
val kotlinVersion: String by System.getProperties()
val kvisionVersion: String by System.getProperties()

// Custom Properties
val webDir = file("src/jsMain/web")
val electronDir = file("src/jsMain/electron")

kotlin {
    js(IR) {
        browser {
            runTask(Action {
                mainOutputFileName = "main.bundle.js"
                sourceMaps = false
                devServer = KotlinWebpackConfig.DevServer(
                    open = false,
                    port = 3000,
                    proxy = mutableMapOf(
                        "/kv/*" to "http://localhost:8080",
                        "/kvws/*" to mapOf("target" to "ws://localhost:8080", "ws" to true)
                    ),
                    static = mutableListOf("${layout.buildDirectory.asFile.get()}/processedResources/js/main")
                )
            })
            webpackTask(Action {
                mainOutputFileName = "main.bundle.js"
            })
            testTask(Action {
                useKarma {
                    useChromeHeadless()
                }
            })
        }
        binaries.executable()
    }
    sourceSets["jsMain"].dependencies {
        implementation(npm("electron-builder", "^23.6.0"))
        implementation("io.kvision:kvision:$kvisionVersion")
        implementation("io.kvision:kvision-bootstrap:$kvisionVersion")
        implementation("io.kvision:kvision-fontawesome:$kvisionVersion")
        implementation("io.kvision:kvision-i18n:$kvisionVersion")
        implementation("io.kvision:kvision-electron:$kvisionVersion")
    }
    sourceSets["jsTest"].dependencies {
        implementation(kotlin("test-js"))
        implementation("io.kvision:kvision-testutils:$kvisionVersion")
    }
}

fun getNodeJsBinaryExecutable(): String {
    val nodeDir =
        org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin.apply(rootProject).nodeJsSetupTaskProvider.get().destination
    val isWindows = System.getProperty("os.name").toLowerCase().contains("windows")
    val nodeBinDir = if (isWindows) nodeDir else nodeDir.resolve("bin")
    val command = org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin.apply(rootProject).nodeCommand
    val finalCommand = if (isWindows && command == "node") "node.exe" else command
    return nodeBinDir.resolve(finalCommand).absolutePath
}

afterEvaluate {
    tasks {
        create("buildApp", Copy::class) {
            dependsOn("jsBrowserProductionWebpack")
            group = "build"
            val distribution =
                project.tasks.getByName("jsBrowserProductionWebpack", KotlinWebpack::class).destinationDirectory
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            from(distribution, webDir, electronDir)
            inputs.files(distribution, webDir, electronDir)
            into("${layout.buildDirectory.asFile.get()}/dist")
        }
        create("runApp", Exec::class) {
            dependsOn("buildApp")
            group = "run"
            workingDir = file("${layout.buildDirectory.asFile.get()}/dist")
            executable = getNodeJsBinaryExecutable()
            args("${rootProject.buildDir}/js/node_modules/electron/cli.js", ".")
        }
        create("bundleApp", Exec::class) {
            dependsOn("buildApp")
            group = "package"
            doFirst {
                val targetDir = file("${layout.buildDirectory.asFile.get()}/electron")
                if (targetDir.exists()) {
                    targetDir.deleteRecursively()
                }
                targetDir.mkdirs()
            }
            workingDir = file("${layout.buildDirectory.asFile.get()}/dist")
            executable = getNodeJsBinaryExecutable()
            args("${rootProject.buildDir}/js/node_modules/electron-builder/out/cli/cli.js", "--config")
        }
    }
}

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().ignoreScripts = false
}
