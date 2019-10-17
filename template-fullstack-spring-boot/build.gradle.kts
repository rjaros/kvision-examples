import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackRunTask
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJsDce
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun


buildscript {
    extra.set("production", (findProperty("prod") ?: findProperty("production") ?: "false") == "true")
    extra.set("kotlin.version", System.getProperty("kotlinVersion"))

    dependencies {
        classpath("pl.treksoft:kvision-gradle-plugin:${System.getProperty("kvisionVersion")}")
    }
}

plugins {
    val kotlinVersion: String by System.getProperties()
    id("kotlinx-serialization") version kotlinVersion
    id("kotlin-multiplatform") version kotlinVersion
    id("io.spring.dependency-management") version System.getProperty("dependencyManagementPluginVersion")
    id("org.springframework.boot") version System.getProperty("springBootVersion")
    kotlin("plugin.spring") version kotlinVersion
    id("kotlin-dce-js") version kotlinVersion
    kotlin("frontend") version System.getProperty("frontendPluginVersion")
}

apply(plugin = "pl.treksoft.kvision")

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
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    mavenLocal()
}

// Versions
val kotlinVersion: String by System.getProperties()
val kvisionVersion: String by System.getProperties()
val springAutoconfigureR2dbcVersion: String by project
val springDataR2dbcVersion: String by project
val r2dbcPostgresqlVersion: String by project
val r2dbcH2Version: String by project
val kweryVersion: String by project

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
                implementation("pl.treksoft:kvision-common-annotations:$kvisionVersion")
            }
            kotlin.srcDir("build/generated-src/common")
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
                implementation("pl.treksoft:kvision-server-spring-boot:$kvisionVersion")
                implementation("org.springframework.boot:spring-boot-starter")
                implementation("org.springframework.boot:spring-boot-devtools")
                implementation("org.springframework.boot:spring-boot-starter-webflux")
                implementation("org.springframework.boot:spring-boot-starter-security")
                implementation("org.springframework.boot.experimental:spring-boot-actuator-autoconfigure-r2dbc:$springAutoconfigureR2dbcVersion")
                implementation("org.springframework.data:spring-data-r2dbc:$springDataR2dbcVersion")
                implementation("io.r2dbc:r2dbc-postgresql:$r2dbcPostgresqlVersion")
                implementation("io.r2dbc:r2dbc-h2:$r2dbcH2Version")
                implementation("com.github.andrewoma.kwery:core:$kweryVersion")
            }
        }
        getByName("backendTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("org.springframework.boot:spring-boot-starter-test")
            }
        }
        getByName("frontendMain") {
            resources.srcDir(webDir)
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("pl.treksoft:kvision-bootstrap:$kvisionVersion")
                implementation("pl.treksoft:kvision-bootstrap-select:$kvisionVersion")
                implementation("pl.treksoft:kvision-bootstrap-datetime:$kvisionVersion")
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
                implementation("pl.treksoft:kvision-remote:$kvisionVersion")
            }
            kotlin.srcDir("build/generated-src/frontend")
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
                into(file(buildDir.path + "/kotlin-js-min/frontend/main"))
            }
        }
    }
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
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
                                    dest: "../src/frontendMain/resources/i18n/",
                                    keywords: ["tr", "ntr:1,2", "gettext", "ngettext:1,2"],
                                    encoding: "UTF-8"
                                },
                                files: {
                                    src: ["../src/frontendMain/kotlin/**/*.kt"],
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
        inputs.files(kotlin.sourceSets["frontendMain"].kotlin.files)
        outputs.file("$projectDir/src/frontendMain/resources/i18n/messages.pot")
    }
}
afterEvaluate {
    tasks {
        getByName("frontendProcessResources", Copy::class) {
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
            into("/public")
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
        getByName("bootJar", BootJar::class) {
            dependsOn("backendMainClasses")
            classpath = files(
                kotlin.targets["backend"].compilations["main"].output.allOutputs +
                        project.configurations["backendRuntimeClasspath"]
            )
        }
        replace("backendJar", BootJar::class).apply {
            dependsOn("frontendJar", "backendMainClasses")
            group = "package"
            archiveAppendix.set("backend")
            classpath = files(
                kotlin.targets["backend"].compilations["main"].output.allOutputs +
                        project.configurations["backendRuntimeClasspath"] +
                        (project.tasks["frontendJar"] as Jar).archiveFile
            )
            mainClassName = "com.example.MainKt"
        }
        replace("jar").apply {
            dependsOn("backendJar")
            group = "package"
            doFirst {
                val bootJar = project.tasks["backendJar"] as BootJar
                val bootFile = bootJar.archiveFile.get().asFile
                val newName = bootFile.name.replace("-${bootJar.archiveAppendix.get()}", "")
                copy {
                    from(bootJar.destinationDirectory.asFile.get().absolutePath)
                    into(bootJar.destinationDirectory.asFile.get().absolutePath)
                    include(bootFile.name)
                    rename(bootFile.name, newName)

                }
            }
        }
        getByName("bootRun", BootRun::class) {
            dependsOn("frontendJar", "backendMainClasses")
            classpath = files(
                kotlin.targets["backend"].compilations["main"].output.allOutputs +
                        project.configurations["backendRuntimeClasspath"] +
                        (project.tasks["frontendJar"] as Jar).archiveFile
            )
        }
        create("frontendRun") {
            dependsOn("webpack-run")
            group = "run"
        }
        create("backendRun") {
            dependsOn("bootRun")
            shouldRunAfter("frontendRun", "webpack-run")
            group = "run"
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
        getByName("compileKotlinBackend") {
            dependsOn("compileKotlinMetadata")
        }
        getByName("compileKotlinFrontend") {
            dependsOn("compileKotlinMetadata")
        }
    }
}

fun KotlinFrontendExtension.webpackBundle(block: WebPackExtension.() -> Unit) =
    bundle("webpack", delegateClosureOf(block))

fun KotlinFrontendExtension.npm(block: NpmExtension.() -> Unit) = configure(block)