plugins {
    kotlin("multiplatform")
}

kotlin {
    jvmToolchain(8)

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
            }
        }
    }

    sourceSets.all {
        languageSettings {
            progressiveMode = true
            optIn("kotlin.ExperimentalStdlibApi")
        }
    }
}
