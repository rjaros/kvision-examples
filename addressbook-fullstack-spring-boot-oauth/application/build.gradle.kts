plugins {
    kotlin("jvm")
    id("org.springframework.boot")
}

dependencies {
    implementation(rootProject)
    implementation(project.dependencies.platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-devtools")
}

springBoot {
    mainClass.value(project.parent?.extra?.get("mainClassName")?.toString())
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    jvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005")
    systemProperties = System.getProperties().toMap() as Map<String, Any>
}
