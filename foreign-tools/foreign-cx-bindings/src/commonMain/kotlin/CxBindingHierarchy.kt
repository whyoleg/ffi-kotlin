package dev.whyoleg.foreign.cx.bindings

import kotlinx.serialization.*

@Serializable
public data class CxBindingHierarchy(
    val modules: Map<String, CxBindingModule>,
    val relations: Map<String, Set<String>>
) {
    public val reversedRelations: Map<String, Set<String>> by lazy {
        buildMap<String, MutableSet<String>> {
            relations.forEach { (key, dependsOn) ->
                dependsOn.forEach {
                    getOrPut(it, ::mutableSetOf).add(key)
                }
            }
        }
    }
}

private fun CxBindingHierarchy.resolve(): Map<String, CxBindingSharedModule> {
    val sharedModules = mutableMapOf<String, CxBindingSharedModule>()

    fun combine(moduleNames: Set<String>): CxBindingSharedModule = moduleNames.map { moduleName ->
        sharedModules.getOrPut(moduleName) {
            modules[moduleName]?.toShared() ?: combine(reversedRelations.getValue(moduleName))
        }
    }.combine()

    reversedRelations.forEach { (sharedModuleName, modulesToShare) ->
        if (sharedModuleName !in sharedModules) {
            sharedModules[sharedModuleName] = combine(modulesToShare)
        }
    }

    return sharedModules
}

/*
test project:
  - common
    - jvmCommon
        - jvm (macosArm64 + macosX64)
        - android (arm64+x64)
    - native
        - apple
            - macos
                - macosX64
                - macosArm64
            - ios
                - 3 targets
        - linux
        - mingw
        - TODO: desktop
    - emscripten (web)
        - wasmJs
        - js
indexes: 3 desktop, 2 android, 3 ios, 1 wasm = 9 - input
kotlin targets: 2 jvm-like, 7 native, 2 web = 11 - intermediate
source sets: 18
 */

/*
processing:
  - generate per target bindings:
    - jvm: generate jvm-macosArm64 and jvm-macosX64 and then combine into jvm
    - android: save as in JVM but for abis
    - native/macosArm64 (or other targets): generate native-macosArm64 (...)
  - start commonization per sourceSet from more specific
    - jvmCommon: commonize jvm and android
    - macos: commonize macosArm64 and macosX64 (same for iOS)
    - apple: commonize macos and ios
    - native: commonize apple, linux and mingw
    - desktop: commonize macos, linux, mingw and jvm
    - common: commonize everything
  - prepare per sourceset declarations
 */
