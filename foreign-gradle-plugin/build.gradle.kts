import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    alias(kotlinLibs.plugins.jvm)
    `java-gradle-plugin`
}

kotlin {
    explicitApi()
    jvmToolchain(8)
//    compilerOptions {
//        apiVersion.set(KotlinVersion.KOTLIN_1_8)
//        languageVersion.set(KotlinVersion.KOTLIN_1_8)
//    }
}

dependencies {
    compileOnly(gradleKotlinDsl())

    compileOnly(kotlinLibs.gradle.plugin)
    compileOnly(libs.build.android)
}
