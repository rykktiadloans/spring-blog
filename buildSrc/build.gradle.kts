plugins {
    `kotlin-dsl`
}

version = "0.0.1-SNAPSHOT"

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("io.spring.gradle:dependency-management-plugin:1.1.7")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.5.0")
    implementation("io.freefair.gradle:lombok-plugin:8.13.1")
}