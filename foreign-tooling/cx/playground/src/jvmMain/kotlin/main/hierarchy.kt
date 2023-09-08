package dev.whyoleg.foreign.cx.playground.main

import dev.whyoleg.foreign.cx.bindings.*
import dev.whyoleg.foreign.cx.index.*

public fun main() {
    testHierarchy.relations.forEach(::println)
    println()
    testHierarchy.reversedRelations.forEach(::println)
}

private val testHierarchy = CxBindingHierarchy(
    modules = mapOf(
        "jvm" to CxBindingTarget.Jvm(
            setOf(
                CxBindingTarget.Jvm.Host.MacosX64,
                CxBindingTarget.Jvm.Host.MacosArm64,
            )
        ),
        "android" to CxBindingTarget.Android(
            setOf(
                CxBindingTarget.Android.Abi.Arm64,
                CxBindingTarget.Android.Abi.X64,
            )
        ),
        "wasmJs" to CxBindingTarget.WasmJs,
        "js" to CxBindingTarget.Js,
        "macosArm64" to CxBindingTarget.Native(
            CxBindingTarget.Native.KonanTarget.MacosArm64
        ),
        "macosX64" to CxBindingTarget.Native(
            CxBindingTarget.Native.KonanTarget.MacosX64
        ),
        "iosArm64" to CxBindingTarget.Native(
            CxBindingTarget.Native.KonanTarget.IosDeviceArm64
        ),
        "iosSimulatorArm64" to CxBindingTarget.Native(
            CxBindingTarget.Native.KonanTarget.IosSimulatorArm64
        ),
        "iosX64" to CxBindingTarget.Native(
            CxBindingTarget.Native.KonanTarget.IosSimulatorX64
        ),
        "linuxX64" to CxBindingTarget.Native(
            CxBindingTarget.Native.KonanTarget.MacosArm64
        ),
        "mingwX64" to CxBindingTarget.Native(
            CxBindingTarget.Native.KonanTarget.MacosArm64
        ),
    ).mapValues {
        CxBindingModule(
            CxIndex(emptyList()),
            it.value,
            emptyMap()
        )
    },
    relations = mapOf(
        "jvm" to setOf("jvmCommon", "desktop"),
        "android" to setOf("jvmCommon"),
        "jvmCommon" to setOf("common"),

        "macosArm64" to setOf("macos"),
        "macosX64" to setOf("macos"),
        "macos" to setOf("apple", "desktop"),
        "iosArm64" to setOf("ios"),
        "iosSimulatorArm64" to setOf("ios"),
        "iosX64" to setOf("ios"),
        "ios" to setOf("apple"),
        "linuxX64" to setOf("native", "desktop"),
        "mingwX64" to setOf("native", "desktop"),
        "apple" to setOf("native"),
        "native" to setOf("common"),
        "desktop" to setOf("common"),


        "wasmJs" to setOf("web"),
        "js" to setOf("web"),
        "web" to setOf("common"),

        "common" to setOf()
    )
)
