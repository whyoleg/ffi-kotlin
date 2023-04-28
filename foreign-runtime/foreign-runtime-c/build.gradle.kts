plugins {
    id("buildx-multiplatform-library")

    id("buildx-target-android")
    id("buildx-target-emscripten")
    id("buildx-target-native-all")
    id("buildx-target-jvm")
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
