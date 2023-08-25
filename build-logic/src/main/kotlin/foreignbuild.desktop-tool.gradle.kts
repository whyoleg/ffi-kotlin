import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    kotlin("multiplatform")
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        languageVersion.finalizeValue()
    }

    targets.configureEach {
        compilations.configureEach {
            compilerOptions.configure {
                // for gradle 8+ support, we need to enforce kotlin 1.8
                apiVersion.set(KotlinVersion.KOTLIN_1_8)
                apiVersion.finalizeValue()
                languageVersion.set(KotlinVersion.KOTLIN_1_8)
                languageVersion.finalizeValue()
            }
        }
    }

    jvm()

    // native desktop targets
    macosArm64()
    macosX64()
    linuxX64()
}
