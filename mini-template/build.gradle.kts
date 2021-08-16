plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("js") version kotlinVersion
}

repositories {
    mavenCentral()
    jcenter()
    mavenLocal()
}

val kvisionVersion: String by System.getProperties()

rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin::class.java) {
  rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().versions.webpackDevServer.version = "4.0.0-rc.0"
}

kotlin {
    js {
        browser {
        }
        binaries.executable()
    }
    sourceSets["main"].dependencies {
        implementation("io.kvision:kvision:$kvisionVersion")
    }
}
