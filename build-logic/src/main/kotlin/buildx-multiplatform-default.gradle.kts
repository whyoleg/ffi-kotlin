@file:OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    id("buildx-multiplatform")
}

kotlin {
    targetHierarchy.custom {
        common {
            group("jvm") {
                withJvm()
            }
        }
    }

    //replace with multi-release JAR
//    val jvmNativeInterface = Attribute.of("jvm.native.interface", String::class.java)
//    jvm("jvmJni") {
//        attributes.attribute(jvmNativeInterface, "JNI")
//    }
    jvm("jvmPanama") {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_19) //20 is not yet supported
            }
        }
//        attributes.attribute(jvmNativeInterface, "Panama")
    }
//    js()
//    wasm()
    //replace later
    macosArm64("native")
}
