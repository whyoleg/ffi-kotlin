package foreignbuild.conventions.multiplatform

plugins {
    kotlin("multiplatform")
}

kotlin {
    targets.configureEach {
        compilations.configureEach {
            compilerOptions.configure {
                progressiveMode.set(true)
                freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
            }
        }
    }
}
