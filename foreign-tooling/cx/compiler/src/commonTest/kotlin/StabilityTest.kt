package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import dev.whyoleg.foreign.tooling.cx.compiler.test.support.*
import kotlin.test.*
import kotlin.time.*

class StabilityTest {
    @Test
    fun test() {
        val targets = listOf(
            CxCompilerTarget.MacosArm64,
            CxCompilerTarget.MacosX64,
            CxCompilerTarget.MingwX64,
            CxCompilerTarget.LinuxX64,
            CxCompilerTarget.IosDeviceArm64,
            CxCompilerTarget.IosSimulatorArm64,
            CxCompilerTarget.IosSimulatorX64,
        )
        val headers = listOf(
            "openssl/ec.h",
            "openssl/bio.h",
            "openssl/evp.h",
            "openssl/bn.h",
            "openssl/aes.h",
            "openssl/types.h",
            "openssl/encoder.h",
            "openssl/ssl.h",
        )
        val runs = buildList {
            repeat(100) {
                targets.forEach { target ->
                    headers.forEach { header ->
                        add(target to header)
                    }
                }
            }
        }

        val indexL = runs.size.toString().length
        val targetL = targets.maxOf { it.toString().length }
        val headerL = headers.maxOf { it.length }

        runs.shuffled().forEachIndexed { index, (target, header) ->
            val (result, time) = measureTimedValue {
                CxCompiler.buildIndexOverOpenssl3(header, target)
            }
            val indexP = (index + 1).toString().padStart(indexL)
            val targetP = target.toString().padEnd(targetL)
            val headerP = header.padEnd(headerL)
            println("[$indexP/${runs.size}] $targetP | $headerP : $time")
            assertTrue(
                result.enums.isNotEmpty() ||
                        result.records.isNotEmpty() ||
                        result.functions.isNotEmpty() ||
                        result.typedefs.isNotEmpty()
            )
        }
    }
}
