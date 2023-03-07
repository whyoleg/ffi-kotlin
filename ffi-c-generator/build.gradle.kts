plugins {
    kotlin("multiplatform")
}

kotlin {
    macosArm64("native") {
        binaries.executable {
            entryPoint = "dev.whyoleg.ffi.c.generator.main"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.ffiCIndex)
            }
        }
    }
}
