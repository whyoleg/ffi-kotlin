import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    alias(kotlinLibs.plugins.jvm)
    `java-gradle-plugin`
}

kotlin {
    jvmToolchain(8)
    explicitApi()
    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_1_8)
    }
}

dependencies {
    compileOnly(gradleKotlinDsl())

    compileOnly(kotlinLibs.gradle.plugin)
    compileOnly(libs.build.android)

    implementation("dev.whyoleg.foreign:foreign-generator-cx")
}
