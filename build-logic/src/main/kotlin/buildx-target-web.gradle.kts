plugins {
    id("buildx-multiplatform")
}

kotlin {
    js {
        nodejs()
    }
    wasm {
        nodejs()
    }
}
