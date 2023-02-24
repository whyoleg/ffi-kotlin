plugins {
    id("buildx-multiplatform-default")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(kotlin("test"))
                api(projects.libraries.libcrypto3.libcrypto3Api)
            }
        }
        jvmMain {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }
}

kotlin {
    wasm {
        nodejs()
    }
    js {
        nodejs()
    }
}
