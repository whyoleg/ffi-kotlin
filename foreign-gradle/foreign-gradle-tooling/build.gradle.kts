import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    alias(kotlinLibs.plugins.jvm)
}

kotlin {
    explicitApi()
    jvmToolchain(8)
    compilerOptions {
        // for compatibility with Gradle 8+
        apiVersion.set(KotlinVersion.KOTLIN_1_8)
        languageVersion.set(KotlinVersion.KOTLIN_1_8)
    }
}
