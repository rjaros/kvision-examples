import org.jetbrains.kotlin.gradle.tasks.KotlinJsDce

plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("js") version kotlinVersion
}

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = uri("https://dl.bintray.com/gbaldeck/kotlin")
        metadataSources {
            mavenPom()
            artifact()
        }
    }
    maven { url = uri("https://dl.bintray.com/rjaros/kotlin") }
}

val kvisionVersion: String by System.getProperties()

kotlin {
    target {
        browser {
        }
    }
    sourceSets["main"].dependencies {
        implementation(kotlin("stdlib-js"))
        implementation("pl.treksoft:kvision:$kvisionVersion")
    }
}

//
// These tasks are necessary to workaround KT-34287
//
tasks {
    withType<KotlinJsDce> {
        doLast {
            copy {
                file("$buildDir/tmp/expandedArchives/").listFiles()?.forEach {
                    if (it.isDirectory && it.name.startsWith("kvision")) {
                        from(it) {
                            include("css/**")
                            include("img/**")
                            include("js/**")
                        }
                    }
                }
                into(file("${buildDir.path}/js/packages/${project.name}/kotlin-dce"))
            }
        }
    }
}
afterEvaluate {
    tasks {
        getByName("processResources", Copy::class) {
            dependsOn("compileKotlinJs")
            doLast {
                copy {
                    file("$buildDir/tmp/expandedArchives/").listFiles()?.forEach {
                        if (it.isDirectory && it.name.startsWith("kvision")) {
                            val kvmodule = it.name.split("-$kvisionVersion").first()
                            from(it) {
                                include("css/**")
                                include("img/**")
                                include("js/**")
                                if (kvmodule == "kvision") {
                                    into("kvision/$kvisionVersion")
                                } else {
                                    into("kvision-$kvmodule/$kvisionVersion")
                                }
                            }
                        }
                    }
                    into(file(buildDir.path + "/js/packages_imported"))
                }
            }
        }
    }
}
