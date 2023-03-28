plugins {
    id("buildx-multiplatform")
}

kotlin {
    js("emscriptenJs") {
        browser()
        nodejs()
    }
    wasm("emscriptenWasm") {
        browser()
        nodejs()
    }
}
