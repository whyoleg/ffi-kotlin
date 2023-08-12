plugins {
    id("foreignbuild.conventions.multiplatform.library")
    id("foreignbuild.conventions.multiplatform.targets.all-with-android")
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
