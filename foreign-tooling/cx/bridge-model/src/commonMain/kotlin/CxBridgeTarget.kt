package dev.whyoleg.foreign.tooling.cx.bridge.model

import kotlinx.serialization.*

// TODO: is it needed? - may be for metadata
// every target represents a single final source set of generated declarations
@Serializable
public sealed class CxBridgeTarget {
    @Serializable
    public sealed class Platform : CxBridgeTarget()

    @Serializable
    public data class Shared(val targets: Set<CxBridgeTarget>) : CxBridgeTarget()

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
