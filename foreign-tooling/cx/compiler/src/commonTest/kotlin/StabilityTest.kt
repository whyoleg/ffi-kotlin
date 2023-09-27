package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.test.support.*
import kotlin.test.*
import kotlin.time.*

class StabilityTest {
    @Test
    fun test() {
        val runs = buildList {
            repeat(100) {
                listOf(
                    CxCompilerTarget.MacosArm64,
                    CxCompilerTarget.MacosX64,
                    CxCompilerTarget.MingwX64,
                    CxCompilerTarget.LinuxX64,
                    CxCompilerTarget.IosDeviceArm64,
                    CxCompilerTarget.IosSimulatorArm64,
                    CxCompilerTarget.IosSimulatorX64,
                ).forEach { target ->
                    listOf(
                        "openssl/ec.h",
                        "openssl/bio.h",
                        "openssl/evp.h",
                        "openssl/bn.h",
                        "openssl/aes.h",
                        "openssl/types.h",
                        "openssl/encoder.h",
                        "openssl/ssl.h",
                    ).forEach { header ->
                        add(target to header)
                    }
                }
            }
        }

        runs.shuffled().forEachIndexed { index, (target, header) ->
            val (result, time) = measureTimedValue {
                CxCompiler.buildIndexOverOpenssl3(header, target)
            }
            val r = runs.size.toString()
            val i = index.toString().padStart(r.length)
            println("[$i/$r] $target|$header: $time")
            assertTrue(
                result.enums.isNotEmpty() ||
                        result.records.isNotEmpty() ||
                        result.functions.isNotEmpty() ||
                        result.typedefs.isNotEmpty()
            )
        }
    }
}
