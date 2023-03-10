plugins {
    id("buildx-multiplatform")

    id("buildx-target-android")
    id("buildx-target-web")
    id("buildx-target-native-all")
    id("buildx-target-jvm-all")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(kotlin("test"))
                api(projects.libraries.libcrypto3.libcrypto3Api)
            }
        }
        jvmAndAndroidMain {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }
}
