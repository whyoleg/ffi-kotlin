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
        CxCompilerTarget.IosDeviceArm64,
        CxCompilerTarget.IosSimulatorArm64,
        CxCompilerTarget.IosSimulatorX64,
    ).associateWith { target ->
        println(target)
        val header = "openssl/bn.h"

        val index = CxCompiler.buildIndexOverOpenssl3(header, target)

//        index.typedefs.forEach { (_, t) ->
//            when (val at = t.data.aliasedType) {
//                is CxCompilerDataType.Record.Reference -> {
//                    val r = index.records.getValue(at.id)
//                    if (r.declarationName == null) {
//                        println(t)
//                        println(r)
//                        println()
//                    }
//                }
//                else                                   -> {}
//            }
//        }


        val (fragment, mapping) = CxBridgeFragment(CxBridgeFragmentId(target.toString()), index) {
            CxBridgeDeclarationId(
                packageName = when (val headerId = headerId) {
                    is CxCompilerHeaderId.Main     -> "openssl.main"
                    is CxCompilerHeaderId.Builtin  -> "openssl.builtin"
                    is CxCompilerHeaderId.Included -> when {
                        headerId.value.startsWith("openssl/") -> "openssl.included"
                        else                                  -> "openssl.internal"
                    }
                },
                declarationName = declarationName
            )
        }

        fragment
    }

    fun sharedFragment(name: String, filter: (CxCompilerTarget) -> Boolean): CxBridgeFragment {
        println(name)
        return CxBridgeFragment(
            fragmentId = CxBridgeFragmentId(name),
            fragments = fragments.filterKeys(filter).mapKeys {
                CxBridgeFragmentId(it.key.toString())
            }
        )
    }

    fun sharedFragment(name: String, fragments: List<CxBridgeFragment>): CxBridgeFragment {
        println(name)
        return CxBridgeFragment(
            fragmentId = CxBridgeFragmentId(name),
            fragments = fragments.associateBy { it.fragmentId }
        )
    }

    val macosFragment = sharedFragment("macos") { it is CxCompilerTarget.Macos }
    val iosFragment = sharedFragment("macos") { it is CxCompilerTarget.Ios }
    val appleFragment = sharedFragment("apple", listOf(macosFragment, iosFragment))
    val desktopFragment = sharedFragment("desktop") { it is CxCompilerTarget.Desktop }
    val nativeFragment = sharedFragment("native", listOf(appleFragment, desktopFragment))

    (fragments.values + listOf(macosFragment, iosFragment, appleFragment, desktopFragment, nativeFragment)).forEach {
//        saveFragment(it)
    }
}

@OptIn(ExperimentalSerializationApi::class)
private val prettyJson = Json {
    prettyPrint = true
    prettyPrintIndent = "  "
}

private fun saveFragment(fragment: CxBridgeFragment) {
    Path("/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/foreign-tooling/cx/bridge-aggregator/build/foreign/fragments/${fragment.fragmentId.value}.json")
        .createParentDirectories()
        .writeText(prettyJson.encodeToString(fragment))
}
