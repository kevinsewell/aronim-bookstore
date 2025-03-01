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
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)

    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    // Development tools
    developmentOnly(libs.spring.boot.devtools)

    // Lombok

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    // Test dependencies

    // Cucumber dependencies
    testImplementation(libs.cucumber.java)
    testImplementation(libs.cucumber.junit)
    testImplementation(libs.cucumber.spring)

    // REST Assured for API testing
    testImplementation(libs.rest.assured)

    // Spring Test dependencies
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)  // Add security test support

    // TestContainers for database testing
    testImplementation(libs.testcontainers.junit)
    // Database
    runtimeOnly(libs.h2.database)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register<JavaExec>("cucumberTests") {
    dependsOn("assemble", "testClasses")
    mainClass.set("io.cucumber.core.cli.Main")
    classpath = configurations.getByName("testRuntimeClasspath") + sourceSets.main.get().output + sourceSets.test.get().output
    args = listOf(
        "--plugin", "pretty",
        "--plugin", "html:build/reports/cucumber/report.html",
        "--glue", "com.aronim.bookstore.cucumber",
        "src/test/resources/features"
    )
}
