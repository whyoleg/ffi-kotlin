plugins {
    id("buildx-multiplatform-library")

//    id("buildx-target-android")
    id("buildx-target-emscripten")
    id("buildx-target-native-all")
    id("buildx-target-jdk-all")

    id("buildx-use-jvm-jni")
}

tasks.named<jni.BuildJni>("buildJni") {
    outputFilePath.set("macos-arm64/libforeign-core-jni.dylib")
}
