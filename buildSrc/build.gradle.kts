plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Add dependencies for plugins you want to use in your build scripts
    // For example:
    // implementation("org.springframework.boot:spring-boot-gradle-plugin:3.2.0")
    // implementation("io.spring.gradle:dependency-management-plugin:1.1.4")
    // implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
}

kotlin {
    jvmToolchain(21)
}
