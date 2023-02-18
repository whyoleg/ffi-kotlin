@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)

plugins {
    id("buildx-multiplatform")
}

kotlin {
    targetHierarchy.default {
        common {
            group("jvm") {
                withJvm()
            }
        }
    }

//    val jvmNativeInterface = Attribute.of("jvm.native.interface", String::class.java)
//    jvm("jvmJni") {
//        attributes.attribute(jvmNativeInterface, "JNI")
//    }
    jvm("jvmPanama") {
//        attributes.attribute(jvmNativeInterface, "Panama")
    }
    macosArm64()
}
