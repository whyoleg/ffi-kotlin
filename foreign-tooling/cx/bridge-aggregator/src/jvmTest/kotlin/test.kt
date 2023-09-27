package dev.whyoleg.foreign.tooling.cx.bridge.aggregator

import dev.whyoleg.foreign.tooling.cx.bridge.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.*
import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.test.support.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.io.path.*

// TODO: move to test
fun main() {
    val fragments = listOf(
        CxCompilerTarget.MacosArm64,
        CxCompilerTarget.MacosX64,
        CxCompilerTarget.MingwX64,
        CxCompilerTarget.LinuxX64,
//        CxCompilerTarget.IosDeviceArm64,
//        CxCompilerTarget.IosSimulatorArm64,
//        CxCompilerTarget.IosSimulatorX64,
    ).associate { target ->
        val dirName = when (target) {
            CxCompilerTarget.MacosArm64 -> "macos-arm64"
            CxCompilerTarget.MacosX64   -> "macos-x64"
            CxCompilerTarget.MingwX64   -> "mingw-x64"
            CxCompilerTarget.LinuxX64   -> "linux-x64"
//            CxCompilerTarget.IosDeviceArm64    -> "ios-device-arm64"
//            CxCompilerTarget.IosSimulatorArm64 -> "ios-simulator-arm64"
//            CxCompilerTarget.IosSimulatorX64   -> "ios-simulator-x64"
        }
        val header = "openssl/bn.h"

        val index = CxCompiler.buildIndexOverOpenssl3(header, target)

        val fragment = CxBridgeFragment(CxBridgeFragmentId(dirName), index) {
            CxBridgeDeclarationId(
                packageName = when (val headerId = headerId) {
                    is CxCompilerHeaderId.Main     -> "openssl.main"
                    is CxCompilerHeaderId.Builtin  -> "openssl.builtin"
                    is CxCompilerHeaderId.Included -> when {
                        headerId.value.startsWith("openssl/") -> "openssl.included"
                        else                                  -> "openssl.internal"
                    }
                },
                declarationName = if (declarationName.isNullOrEmpty()) "FIX: ${id.value}" else declarationName!! // TODO: what to do here?
            )
        }

        saveFragment(fragment)

        CxBridgeFragmentId(dirName) to fragment
    }

    val sharedFragment = CxBridgeFragment(
        CxBridgeFragmentId("shared"),
        fragments
    )

    saveFragment(sharedFragment)
}

private val prettyJson = Json {
    prettyPrint = true
    prettyPrintIndent = "  "
}

private fun saveFragment(fragment: CxBridgeFragment) {
    Path("/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-tooling/cx/bridge-aggregator/build/foreign/fragments/${fragment.fragmentId.value}.json")
        .createParentDirectories()
        .writeText(prettyJson.encodeToString(fragment))
}
