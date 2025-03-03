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

    // Spring OAuth2 dependencies
    implementation(libs.spring.boot.starter.oauth2.client)
    implementation(libs.spring.boot.starter.oauth2.resource.server)

    // Spring Test dependencies
    implementation(libs.spring.boot.starter.test)
    implementation(libs.spring.security.test)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
