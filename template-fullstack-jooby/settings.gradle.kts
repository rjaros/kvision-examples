pluginManagement {
    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven { url = uri("https://dl.bintray.com/rjaros/kotlin") }
        mavenLocal()
    }
    resolutionStrategy {
        eachPlugin {
            when {
                requested.id.id == "kotlinx-serialization" -> useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
                requested.id.id == "jooby" -> useModule("io.jooby:jooby-gradle-plugin:${requested.version}")
                requested.id.id == "kvision" -> useModule("pl.treksoft:kvision-gradle-plugin:${requested.version}")
            }
        }
    }
}
rootProject.name = "template-fullstack-jooby"
