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
	}
}

dependencies {

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

    // Lombok dependencies

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Test dependencies

    // Spring Test dependencies
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)

    // Database dependencies
    runtimeOnly(libs.h2.database)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
