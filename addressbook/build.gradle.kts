import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.util.nodePath
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
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
    id("kotlin2js") version kotlinVersion
    id("kotlin-dce-js") version kotlinVersion
    kotlin("frontend") version System.getProperty("frontendPluginVersion")
}

repositories {
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    maven { url = uri("https://dl.bintray.com/gbaldeck/kotlin") }
    maven { url = uri("https://dl.bintray.com/rjaros/kotlin") }
    mavenLocal()
}

version = "1.0.0-SNAPSHOT"
group = "com.example"

// Versions
val kotlinVersion: String by System.getProperties()
val ktorVersion: String by project
val logbackVersion: String by project
val kvisionVersion: String by project

// Custom Properties
val webDir = file("src/main/web")
val isProductionBuild = project.extra.get("production") as Boolean
//val mainClassName = "io.ktor.server.netty.EngineMain"

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("pl.treksoft:kvision:$kvisionVersion")
    implementation("pl.treksoft:kvision-bootstrap:$kvisionVersion")
    implementation("pl.treksoft:kvision-select:$kvisionVersion")
    implementation("pl.treksoft:kvision-i18n:$kvisionVersion")
    implementation("pl.treksoft:kvision-datacontainer:$kvisionVersion")
    testImplementation(kotlin("test-js"))
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
        contentPath = webDir
        mode = if (isProductionBuild) "production" else "development"
    }

    define("PRODUCTION", isProductionBuild)
}
sourceSets["main"].resources.srcDir(webDir)

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
    }
    getByName("processResources", Copy::class) {
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
                                    dest: "$buildDir/resources/main/i18n/",
                                    keywords: ["tr", "ntr:1,2", "gettext", "ngettext:1,2"],
                                    encoding: "UTF-8"
                                },
                                files: {
                                    src: ["$projectDir/src/main/kotlin/**/*.kt"],
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
    create("generatePotFile", NodeJsExec::class) {
        dependsOn("npm-install", "generateNpmScripts", "generateGruntfile")
        workingDir = file("$buildDir")
        args(nodePath(project, "npm").first().absolutePath, "run", "pot")
        inputs.files(sourceSets["main"].allSource)
        outputs.file("$buildDir/resources/main/i18n/messages.pot")
    }
}
afterEvaluate {
    tasks {
        getByName("npm-configure").shouldRunAfter("generateNpmScripts")
        getByName("webpack-run").dependsOn("classes")
        getByName("webpack-bundle").dependsOn("classes", "runDceKotlinJs")
        replace("jar", Jar::class).apply {
            dependsOn("webpack-bundle")
            from(project.tasks["webpack-bundle"].outputs)
            from(project.tasks["processResources"].outputs)
            inputs.files(project.tasks["webpack-bundle"].outputs)
            inputs.files(project.tasks["processResources"].outputs)
            outputs.file("$destinationDirectory/$archiveFileName")

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
        create("zip", Zip::class) {
            dependsOn("webpack-bundle")
            from(project.tasks["webpack-bundle"].outputs)
            from(project.tasks["processResources"].outputs)
            inputs.files(project.tasks["webpack-bundle"].outputs)
            inputs.files(project.tasks["processResources"].outputs)
            destinationDirectory.set(file("$buildDir/libs"))
            outputs.file("$destinationDirectory/$archiveFileName")
        }
    }
}

fun KotlinFrontendExtension.webpackBundle(block: WebPackExtension.() -> Unit) =
        bundle("webpack", delegateClosureOf(block))

fun KotlinFrontendExtension.npm(block: NpmExtension.() -> Unit) = configure(block)