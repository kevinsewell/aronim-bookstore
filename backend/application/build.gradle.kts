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
    implementation(project(":aronim-bookstore-backend-platform-security"))

    // Flyway dependencies
    implementation(libs.flyway)

    // H2 dependencies

    runtimeOnly(libs.h2.database)

    // PostgreSQL dependencies

    runtimeOnly(libs.postgresql)

    // Lombok dependencies

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Spring dependencies

    developmentOnly(libs.spring.boot.devtools)

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)

    // Spring Doc dependencies
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    // Test dependencies

    // Spring Test dependencies
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
