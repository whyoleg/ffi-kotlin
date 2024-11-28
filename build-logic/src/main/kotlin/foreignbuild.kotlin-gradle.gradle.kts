import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    id("foreignbuild.kotlin")
    kotlin("jvm")
}

kotlin {
    // JVM for Gradle plugin
    jvmToolchain(17)

    // gradle 8+
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_1_8
        apiVersion = KotlinVersion.KOTLIN_1_8
        progressiveMode = false
    }
}
