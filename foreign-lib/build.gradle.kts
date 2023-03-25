plugins {
    id("buildx-multiplatform-library")

//    id("buildx-target-android")
    id("buildx-target-web")
    id("buildx-target-native-all")
    id("buildx-target-jvm-all")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.foreignC)
            }
        }
    }
}
