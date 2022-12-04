import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("plugin.serialization") version kotlinVersion
    kotlin("js") version kotlinVersion
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
val webDir = file("src/main/web")
val electronDir = file("src/main/electron")

kotlin {
    js {
        browser {
            runTask {
                outputFileName = "main.bundle.js"
                sourceMaps = false
                devServer = KotlinWebpackConfig.DevServer(
                    open = false,
                    port = 3000,
                    proxy = mutableMapOf(
                        "/kv/*" to "http://localhost:8080",
                        "/kvws/*" to mapOf("target" to "ws://localhost:8080", "ws" to true)
                    ),
                    static = mutableListOf("$buildDir/processedResources/js/main")
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
    sourceSets["main"].dependencies {
        implementation(npm("electron-builder", "^23.6.0"))
        implementation("io.kvision:kvision:$kvisionVersion")
        implementation("io.kvision:kvision-bootstrap:$kvisionVersion")
        implementation("io.kvision:kvision-fontawesome:$kvisionVersion")
        implementation("io.kvision:kvision-i18n:$kvisionVersion")
        implementation("io.kvision:kvision-electron:$kvisionVersion")
    }
    sourceSets["test"].dependencies {
        implementation(kotlin("test-js"))
        implementation("io.kvision:kvision-testutils:$kvisionVersion")
    }
    sourceSets["main"].resources.srcDir(webDir)
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
            dependsOn("browserProductionWebpack")
            group = "build"
            val distribution =
                project.tasks.getByName("browserProductionWebpack", KotlinWebpack::class).destinationDirectory
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            from(distribution, webDir, electronDir)
            inputs.files(distribution, webDir, electronDir)
            into("$buildDir/dist")
        }
        create("runApp", Exec::class) {
            dependsOn("buildApp")
            group = "run"
            workingDir = file("$buildDir/dist")
            executable = getNodeJsBinaryExecutable()
            args("${rootProject.buildDir}/js/node_modules/electron/cli.js", ".")
        }
        create("bundleApp", Exec::class) {
            dependsOn("buildApp")
            group = "package"
            doFirst {
                val targetDir = file("$buildDir/electron")
                if (targetDir.exists()) {
                    targetDir.deleteRecursively()
                }
                targetDir.mkdirs()
            }
            workingDir = file("$buildDir/dist")
            executable = getNodeJsBinaryExecutable()
            args("${rootProject.buildDir}/js/node_modules/electron-builder/out/cli/cli.js", "--config")
        }
    }
}

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().ignoreScripts = false
}
