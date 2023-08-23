plugins {
    id("foreignbuild.multiplatform-library")
    id("foreignbuild.multiplatform-all-targets")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.foreignRuntime.foreignRuntimeCore)
            }
        }
    }
}
