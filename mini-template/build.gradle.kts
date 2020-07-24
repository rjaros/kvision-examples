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
    mavenLocal()
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
