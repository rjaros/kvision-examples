plugins {
    val kotlinVersion: String by System.getProperties()
    kotlin("js") version kotlinVersion
}

repositories {
    mavenCentral()
    jcenter()
    maven { url = uri("https://dl.bintray.com/rjaros/kotlin") }
    mavenLocal()
}

val kvisionVersion: String by System.getProperties()

kotlin {
    js {
        browser {
        }
    }
    sourceSets["main"].dependencies {
        implementation("pl.treksoft:kvision:$kvisionVersion")
    }
}
