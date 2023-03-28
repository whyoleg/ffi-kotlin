plugins {
    id("buildx-multiplatform")
}

kotlin {
    js("webJs") {
        browser()
        nodejs()
    }
    wasm("webWasm") {
        browser()
        nodejs()
    }
}
