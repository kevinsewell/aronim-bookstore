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

    api(project(":aronim-bookstore-backend-platform-core"))
    api(project(":aronim-bookstore-backend-platform-security"))

    // Flyway dependencies
    api(libs.flyway)

    // Lombok dependencies

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Spring dependencies
    api(libs.spring.boot.starter.actuator)
    api(libs.spring.boot.starter.data.jpa)
    api(libs.spring.boot.starter.validation)
    api(libs.spring.boot.starter.web)

    // Spring Doc dependencies
    api(libs.springdoc.openapi.starter.webmvc.ui)

    // Test dependencies

    testImplementation(project(":aronim-bookstore-backend-platform-security-test"))

    // H2 Database dependencies
    testImplementation(libs.h2.database)

}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
