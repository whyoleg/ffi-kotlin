plugins {
    id("buildx-multiplatform-default")
    id("buildx-multiplatform-library")
}

tasks.named<jni.BuildJni>("buildJni") {
    outputFilePath.set("macos-arm64/libffi-jni.dylib")
}

kotlin {
    js {
        nodejs()
    }
    wasm {
        nodejs()
    }
}
