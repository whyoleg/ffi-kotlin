package dev.whyoleg.foreign.cx.bindings

import kotlinx.serialization.*
import kotlin.jvm.*

@Serializable
@JvmInline
public value class CxBindingTargetId(public val value: String)

// every target represents a single final source set of generated declarations
@Serializable
public sealed class CxBindingTarget {
    @Serializable
    public sealed class Platform : CxBindingTarget()

    @Serializable
    public data class Shared(val targets: Set<CxBindingTarget>) : CxBindingTarget()

    @Serializable
    public data class Jvm(val hosts: Set<Host>) : Platform() {
        @Serializable
        public enum class Host { MacosArm64, MacosX64, LinuxX64, MingwX64 }
    }

    @Serializable
    public data class Android(val abis: Set<Abi>) : Platform() {
        @Serializable
        public enum class Abi { Arm64, X64 }
    }

    @Serializable
    public data object WasmJs : Platform()

    @Serializable
    public data object Js : Platform()

    @Serializable
    public data class Native(val konanTarget: KonanTarget) : Platform() {
        @Serializable
        public enum class KonanTarget {
            MacosArm64, MacosX64, LinuxX64, MingwX64,
            IosDeviceArm64, IosSimulatorArm64, IosSimulatorX64
        }
    }
}

/*

targets:
  - jvm (single artifact) - jni and/or panama
    - macos-x64
    - macos-arm64
    - linux-x64
    - mingw-x64 or windows-x64 - TBD
  - android (single artifact) - jni only
    - arm64
    - x64
    - ...
  - wasmWasi (nodejs and browser?)
    - wasm32 or wasm64 or?
  - wasmJs (nodejs and browser)
    - wasm32 or wasm64
  - js (nodejs and browser)
    - wasm32 or wasm64
  - native
    - macos-x64
    - macos-arm64
    - linux-x64
    - mingw-x64
    - ios-device-arm64
    - ios-simulator-arm64
    - ios-simulator-x64
    - ...

 */
