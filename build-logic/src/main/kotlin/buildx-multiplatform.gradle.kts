plugins {
    kotlin("multiplatform")
}

kotlin {
    //TODO: setup 20 toolchain only for panama compilation - is it possible?
    jvmToolchain(20) //for panama

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
