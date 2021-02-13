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
