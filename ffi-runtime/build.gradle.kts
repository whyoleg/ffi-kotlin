plugins {
    id("buildx-multiplatform-library")

    id("buildx-target-web")
    id("buildx-target-native-all")
    id("buildx-target-jvm-all")

    id("buildx-use-jvm-jni")
}

tasks.named<jni.BuildJni>("buildJni") {
    outputFilePath.set("macos-arm64/libffi-jni.dylib")
}
