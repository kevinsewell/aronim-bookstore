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

    // Spring Security dependencies

    api(libs.spring.boot.starter.oauth2.client)
    api(libs.spring.boot.starter.oauth2.resource.server)
    api(libs.spring.boot.starter.security)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
