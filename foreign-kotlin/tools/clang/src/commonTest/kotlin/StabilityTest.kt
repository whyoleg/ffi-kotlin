package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.clang.testenv.*
import kotlin.test.*
import kotlin.time.*

class StabilityTest {
    @Test
    fun test() {
        val targets = listOf(
            ClangTarget.MacosArm64,
            ClangTarget.MacosX64,
            ClangTarget.MingwX64,
            ClangTarget.LinuxX64,
            ClangTarget.IosDeviceArm64,
            ClangTarget.IosSimulatorArm64,
            ClangTarget.IosSimulatorX64,
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
                indexWithOpenssl3Headers(target, setOf(header))
            }
            val indexP = (index + 1).toString().padStart(indexL)
            val targetP = target.toString().padEnd(targetL)
            val headerP = header.padEnd(headerL)
            println("[$indexP/${runs.size}] $targetP | $headerP : $time")
            assertTrue(result.declarations.isNotEmpty())
        }
    }
}
