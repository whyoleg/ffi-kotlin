plugins {
    id("buildx-multiplatform-default")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.ffiRuntime)
            }
        }
    }
}
