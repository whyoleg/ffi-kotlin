package foreignbuild.conventions.multiplatform

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvmToolchain(8)

    targets.configureEach {
        compilations.configureEach {
            compilerOptions.configure {
                progressiveMode.set(true)
                freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
            }
        }
    }

    sourceSets.configureEach {
        languageSettings {
            // optIn in compilations are not propagated to IDE
            optIn("kotlin.ExperimentalStdlibApi")
            optIn("kotlinx.cinterop.ExperimentalForeignApi")
        }
    }
}
