plugins {
    id("foreignbuild.conventions.multiplatform.library")
    id("foreignbuild.conventions.multiplatform.targets.all")
    id("foreignbuild.conventions.multiplatform.targets.android")
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
