import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    id("foreignbuild.kotlin")
    kotlin("jvm")
    `java-gradle-plugin`
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        // gradle 8+
        languageVersion.set(KotlinVersion.KOTLIN_1_8)
        apiVersion.set(KotlinVersion.KOTLIN_1_8)
    }
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("dev.whyoleg.foreign") {
            id = "dev.whyoleg.foreign"
            implementationClass = "dev.whyoleg.foreign.gradle.ForeignPlugin"
        }
    }
}
