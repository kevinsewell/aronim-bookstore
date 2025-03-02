import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("java-library")
    alias(libs.plugins.spring.boot).apply(false)
    alias(libs.plugins.spring.dependency.management)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

the<DependencyManagementExtension>().apply {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
		mavenBom("org.springframework.modulith:spring-modulith-bom:1.3.3")
	}
}

dependencies {

    implementation(project(":aronim-bookstore-backend-platform-core"))

    // Flyway dependencies
    implementation(libs.flyway)

    // Spring dependencies
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)

    // Spring Doc dependencies
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    // Spring Modulith dependencies
    testImplementation(libs.spring.modulith.starter.core)

    // Lombok dependencies

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Test dependencies

    // Spring Test dependencies
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)

    // Spring Modulith Test dependencies
    testImplementation(libs.spring.modulith.starter.test)

    // Database dependencies
    runtimeOnly(libs.h2.database)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
