@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.konan.target.*

plugins {
    id("buildx-multiplatform-base")
}

kotlin {
    fun KotlinTargetHierarchyBuilder.withPlatform(platformType: KotlinPlatformType, block: (KotlinTarget) -> Boolean = { true }) {
        withCompilations { it.target.platformType == platformType && block(it.target) }
    }

    targetHierarchy.custom {
        common {
            group("native") {
                withNative()
            }
            group("nonNative") {
                withCompilations { it.target.platformType != KotlinPlatformType.native }
            }
            group("platformInt") {
                //shares common wasm api
                group("emscripten") {
                    withPlatform(KotlinPlatformType.js)
                    withPlatform(KotlinPlatformType.wasm)
                }
                withPlatform(KotlinPlatformType.native) {
                    (it as KotlinNativeTarget).konanTarget == KonanTarget.IOS_ARM32
                }
            }
            group("platformLong") {
                withPlatform(KotlinPlatformType.native) {
                    (it as KotlinNativeTarget).konanTarget != KonanTarget.IOS_ARM32
                }

                //shares a library loading mechanism and some of the platform specifics
                group("jvmCommon") {
                    withAndroid()
                    withJvm()
                }
            }
        }
    }
}
