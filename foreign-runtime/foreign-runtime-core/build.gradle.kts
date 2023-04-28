plugins {
    id("buildx-multiplatform-library")

    id("buildx-target-android")
    id("buildx-target-emscripten")
    id("buildx-target-native-all")
    id("buildx-target-jvm")

    id("buildx-use-android-jni")
}

val buildJni by tasks.registering(jni.DefaultBuildJni::class) {
    outputFilePath.set("macos-arm64/libforeign-kotlin-jni.dylib")
}

kotlin {
    sourceSets {
        val jvmCommonMain by getting {
            resources.srcDir(buildJni.map { it.outputDirectory })
        }
    }
}
