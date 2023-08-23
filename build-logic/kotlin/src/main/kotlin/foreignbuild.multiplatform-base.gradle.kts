import org.jetbrains.kotlin.gradle.plugin.*

plugins {
    kotlin("multiplatform")
}

kotlin {
    targets.configureEach {
        compilations.configureEach {
            compilerOptions.configure {
                progressiveMode.set(true)
                freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
                if (platformType == KotlinPlatformType.jvm) {
                    freeCompilerArgs.add("-Xjvm-default=all")
                }
            }
        }
    }

    // TODO: move where needed
    sourceSets.configureEach {
        languageSettings {
            // optIn in compilations are not propagated to IDE
            optIn("kotlin.ExperimentalStdlibApi")
            optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
    }
}
