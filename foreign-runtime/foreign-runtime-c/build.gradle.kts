plugins {
    id("foreignbuild.conventions.multiplatform.library")
    id("foreignbuild.conventions.multiplatform.targets.runtime.all")
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
