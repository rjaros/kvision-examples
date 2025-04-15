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
            useEsModules()
        }
        binaries.executable()
        compilerOptions {
            target.set("es2015")
        }
    }
    sourceSets["jsMain"].dependencies {
        implementation("io.kvision:kvision:$kvisionVersion")
    }
}
