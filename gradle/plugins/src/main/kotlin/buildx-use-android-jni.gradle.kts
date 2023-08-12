plugins {
    id("buildx-target-android")
}

android {
    //ndk for JNI setup
    ndkVersion = "25.2.9519653"
    defaultConfig {
        ndk {
            abiFilters += listOf("x86_64", "arm64-v8a")
        }
    }
    externalNativeBuild {
        ndkBuild {
            path("src/jvmAndroidMain/jni/Android.mk")
        }
    }
}
