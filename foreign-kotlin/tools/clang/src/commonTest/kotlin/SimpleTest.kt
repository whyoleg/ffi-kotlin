package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.clang.api.*
import dev.whyoleg.foreign.tool.clang.testenv.*
import dev.whyoleg.foreign.tool.serialization.*
import kotlinx.io.files.*
import kotlin.test.*
import kotlin.time.*

class SimpleTest {

    val tempPath = Path("build/foreign-temp")

    @Test
    fun testSingle() {
        listOf(
            ClangTarget.MacosArm64,
            ClangTarget.MacosX64,
            ClangTarget.MingwX64,
            ClangTarget.LinuxX64,
            ClangTarget.IosDeviceArm64,
            ClangTarget.IosSimulatorArm64,
            ClangTarget.IosSimulatorX64,
        ).forEach { target ->
            val index = indexWithOpenssl3Headers(target, setOf("openssl/bn.h"))
            val filteredIndex = index.filter(
                includedHeaderPatterns = listOf(Regex("openssl/bn\\.h"))
            )
            Path(tempPath, "$target/bn/index.json").encode(index)
            Path(tempPath, "$target/bn/index.filtered.json").encode(filteredIndex)
        }
    }

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
            targets.forEach { target ->
                headers.forEach { header ->
                    add(target to header)
                }
            }
        }

        val indexL = runs.size.toString().length
        val targetL = targets.maxOf { it.toString().length }
        val headerL = headers.maxOf { it.length }

        runs.forEachIndexed { index, (target, header) ->
            val indexP = (index + 1).toString().padStart(indexL)
            val targetP = target.toString().padEnd(targetL)
            val headerP = header.padEnd(headerL)

            println("[$indexP/${runs.size}] $targetP | $headerP: START")
            val (result, time) = measureTimedValue {
                indexWithOpenssl3Headers(target, setOf(header))
            }
            println("[$indexP/${runs.size}] $targetP | $headerP : $time")
            assertTrue(result.declarations.isNotEmpty())
            Path(tempPath, "ALL/$target/${header}.json").encode(result)
        }
    }
}
