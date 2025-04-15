@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "addressbook-fullstack-spring-boot"
include(":application")
