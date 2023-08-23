plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.multiplatform-all-targets")
}

kotlin {
    jvmToolchain(8)
    sourceSets {
        commonMain {
            dependencies {
                api(projects.foreignRuntime.foreignRuntimeCore)
            }
        }
    }
}
