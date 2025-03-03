plugins {
    id("java")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":aronim-bookstore-backend-module-catalog"))

    // Flyway dependencies
    implementation(libs.flyway)

    // Spring dependencies
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.oauth2.client)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)

    // Spring Doc dependencies
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    // Development tools dependencies
    developmentOnly(libs.spring.boot.devtools)

    // Lombok dependencies

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Test dependencies

    // Spring Test dependencies
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)  // Add security test support

    // Database dependencies
    runtimeOnly(libs.h2.database)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
