pluginManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        mavenLocal()
    }
    resolutionStrategy {
        eachPlugin {
            when {
                requested.id.id == "kotlinx-serialization" -> useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
                requested.id.id == "org.springframework.boot" -> useModule("org.springframework.boot:spring-boot-gradle-plugin:${requested.version}")
                requested.id.id == "kvision" -> useModule("io.kvision:kvision-gradle-plugin:${requested.version}")
            }
        }
    }
}
rootProject.name = "addressbook-fullstack-spring-boot"
