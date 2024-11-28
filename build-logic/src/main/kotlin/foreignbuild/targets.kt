/*
 * Copyright 2015-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package foreignbuild

import org.jetbrains.kotlin.gradle.*
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.targets.jvm.*

fun KotlinMultiplatformExtension.nativeTargets() {
    macosX64()
    macosArm64()

    iosArm64()
    iosX64()
    iosSimulatorArm64()

    watchosX64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    watchosDeviceArm64()

    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()

    linuxX64()
    linuxArm64()

    mingwX64()

    androidNativeX64()
    androidNativeX86()
    androidNativeArm64()
    androidNativeArm32()
}

// TODO: wasm wasi
@OptIn(ExperimentalWasmDsl::class)
fun KotlinMultiplatformExtension.webTargets() {
    js {
        nodejs()
        browser()
    }
    wasmJs {
        nodejs()
        browser()
    }
}

fun KotlinMultiplatformExtension.jvmTarget(
    jdkTestVersions: Set<Int> = setOf(8, 11, 17, 21, 22, 23),
    configure: KotlinJvmTarget.() -> Unit = {}
) {
    jvm {
        configure()
//        val javaToolchains = project.extensions.getByName<JavaToolchainService>("javaToolchains")
//
//        jdkAdditionalTestVersions.forEach { jdkTestVersion ->
//            testRuns.create("${jdkTestVersion}Test") {
//                executionTask.configure {
//                    javaLauncher.set(javaToolchains.launcherFor {
//                        languageVersion.set(JavaLanguageVersion.of(jdkTestVersion))
//                    })
//                }
//            }
//        }
    }
}
