@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    id("buildx-multiplatform")
    id("buildx-multiplatform-jvm-jni")
    id("buildx-multiplatform-jvm-panama")
}

kotlin {
    targetHierarchy.custom {
        common {
            group("jvm") {
                withJvm()
            }
        }
    }
    //replace later with full-blown hierarchy
    macosArm64("native")
//    js()
//    wasm()
}
