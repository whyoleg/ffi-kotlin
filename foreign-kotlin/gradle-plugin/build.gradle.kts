import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
}

kotlin {
    explicitApi()
    jvmToolchain(17)
    compilerOptions {
        allWarningsAsErrors.set(true)
        freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
        freeCompilerArgs.add("-Xjvm-default=all")

        // gradle 8+
        languageVersion.set(KotlinVersion.KOTLIN_1_8)
        apiVersion.set(KotlinVersion.KOTLIN_1_8)
    }
}

dependencies {
    compileOnly(kotlin("stdlib")) // TODO?
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("dev.whyoleg.foreign") {
            id = "dev.whyoleg.foreign"
            implementationClass = "dev.whyoleg.foreign.gradle.plugin.ForeignPlugin"
        }
    }
}
