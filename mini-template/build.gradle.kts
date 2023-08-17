plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("multiplatform") version kotlinVersion
}

repositories {
    mavenCentral()
    mavenLocal()
}

val kvisionVersion: String by System.getProperties()

kotlin {
    js(IR) {
        browser {
        }
        binaries.executable()
    }
    sourceSets["jsMain"].dependencies {
        implementation("io.kvision:kvision:$kvisionVersion")
    }
}
