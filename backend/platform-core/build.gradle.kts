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

    // Lombok dependencies

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Spring dependencies
    implementation(libs.spring.context)

    // Spring Doc dependencies
    api(libs.springdoc.openapi.starter.webmvc.ui)

}
