import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
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

version = "1.0.0-SNAPSHOT"
group = "com.example"

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
    maven { url = uri("https://dl.bintray.com/gbaldeck/kotlin") }
    maven { url = uri("https://dl.bintray.com/rjaros/kotlin") }
    mavenLocal()
}

// Versions
val kotlinVersion: String by System.getProperties()
val kvisionVersion: String by System.getProperties()

// Custom Properties
val webDir = file("src/main/web")
val isProductionBuild = project.extra.get("production") as Boolean

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("pl.treksoft:kvision:$kvisionVersion")
    implementation("pl.treksoft:kvision-bootstrap:$kvisionVersion")
    implementation("pl.treksoft:kvision-bootstrap-css:$kvisionVersion")
    implementation("pl.treksoft:kvision-bootstrap-datetime:$kvisionVersion")
    implementation("pl.treksoft:kvision-bootstrap-select:$kvisionVersion")
    implementation("pl.treksoft:kvision-bootstrap-spinner:$kvisionVersion")
    implementation("pl.treksoft:kvision-bootstrap-upload:$kvisionVersion")
    implementation("pl.treksoft:kvision-bootstrap-dialog:$kvisionVersion")
    implementation("pl.treksoft:kvision-fontawesome:$kvisionVersion")
    implementation("pl.treksoft:kvision-i18n:$kvisionVersion")
    implementation("pl.treksoft:kvision-richtext:$kvisionVersion")
    implementation("pl.treksoft:kvision-handlebars:$kvisionVersion")
    implementation("pl.treksoft:kvision-datacontainer:$kvisionVersion")
    implementation("pl.treksoft:kvision-redux:$kvisionVersion")
    implementation("pl.treksoft:kvision-chart:$kvisionVersion")
    implementation("pl.treksoft:kvision-tabulator:$kvisionVersion")
    implementation("pl.treksoft:kvision-pace:$kvisionVersion")
    implementation("pl.treksoft:kvision-moment:$kvisionVersion")
    testImplementation(kotlin("test-js"))
    testImplementation("pl.treksoft:kvision-testutils:$kvisionVersion:tests")
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
        inputs.property("production", isProductionBuild)
        doFirst {
            destinationDir.deleteRecursively()
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
                into(file(buildDir.path + "/kotlin-js-min/main"))
            }
        }
    }
    create("generateGruntfile") {
        outputs.file("$buildDir/Gruntfile.js")
        doLast {
            file("$buildDir/Gruntfile.js").run {
                writeText(
                    """
                    module.exports = function (grunt) {
                        grunt.initConfig({
                            pot: {
                                options: {
                                    text_domain: "messages",
                                    dest: "../src/main/resources/i18n/",
                                    keywords: ["tr", "ntr:1,2", "gettext", "ngettext:1,2"],
                                    encoding: "UTF-8"
                                },
                                files: {
                                    src: ["../src/main/kotlin/**/*.kt"],
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
        dependsOn("npm-install", "generateGruntfile")
        workingDir = file("$buildDir")
        executable = NodeJsRootPlugin.apply(project).nodeCommand
        args("$buildDir/node_modules/grunt/bin/grunt", "pot")
        inputs.files(sourceSets["main"].allSource)
        outputs.file("$projectDir/src/main/resources/i18n/messages.pot")
    }
}
afterEvaluate {
    tasks {
        getByName("processResources", Copy::class) {
            dependsOn("npm-install")
            exclude("**/*.pot")
            doLast("Convert PO to JSON") {
                destinationDir.walkTopDown().filter {
                    it.isFile && it.extension == "po"
                }.forEach {
                    exec {
                        executable = NodeJsRootPlugin.apply(project).nodeCommand
                        args(
                            "$buildDir/node_modules/po2json/bin/po2json",
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
        getByName("webpack-run").dependsOn("classes")
        getByName("webpack-bundle").dependsOn("classes", "runDceKotlinJs")
        create("webJar", Jar::class) {
            dependsOn("webpack-bundle")
            group = "package"
            val from = project.tasks["webpack-bundle"].outputs.files + webDir
            from(from)
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
        create("zip", Zip::class) {
            dependsOn("webpack-bundle")
            group = "package"
            destinationDirectory.set(file("$buildDir/libs"))
            val from = project.tasks["webpack-bundle"].outputs.files + webDir
            from(from)
            inputs.files(from)
            outputs.file(archiveFile)
        }
    }
}

fun KotlinFrontendExtension.webpackBundle(block: WebPackExtension.() -> Unit) =
    bundle("webpack", delegateClosureOf(block))

fun KotlinFrontendExtension.npm(block: NpmExtension.() -> Unit) = configure(block)
