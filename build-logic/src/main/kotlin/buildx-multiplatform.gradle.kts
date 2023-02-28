@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.*

plugins {
    kotlin("multiplatform")
}

kotlin {
    //TODO: setup 20 toolchain only for panama compilation - is it possible?
    jvmToolchain(20) //for panama

    fun KotlinTargetHierarchyBuilder.withPlatform(platformType: KotlinPlatformType, block: (KotlinTarget) -> Boolean = { true }) {
        withCompilations {
            it.target.platformType == platformType &&
                    block(it.target)
        }
    }

    fun KotlinTargetHierarchyBuilder.withWasm() {
        withPlatform(KotlinPlatformType.wasm)
    }

    //TODO: what is a better way if not by name of target?
    fun KotlinTargetHierarchyBuilder.withJvmJni() {
        withPlatform(KotlinPlatformType.jvm) { it.name.contains("jni", ignoreCase = true) }
    }

    fun KotlinTargetHierarchyBuilder.withJvmPanama() {
        withPlatform(KotlinPlatformType.jvm) { it.name.contains("panama", ignoreCase = true) }
    }
    //TODO: replace later with full-blown hierarchy based on `default` when more native targets will be added
    targetHierarchy.custom {
        common {
            group("jni") {
                withAndroid()
                withJvmJni()
            }
            group("jvm") {
                withJvmJni()
                withJvmPanama()
            }
            group("web") {
                withJs()
                withWasm()
            }

            //group, on which jni and web depends on -> no native, no panama, raw memory access
            //TODO: work on it later
//            group("raw") {
//                group("jni")
//                group("web")
//            }
        }
    }

    targets.all {
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xrender-internal-diagnostic-names")
            }
        }
    }

    sourceSets.all {
        languageSettings {
            progressiveMode = true
            optIn("kotlin.ExperimentalStdlibApi")
        }
    }
}
