package dev.whyoleg.foreign.tool.clang

import dev.whyoleg.foreign.tool.clang.api.*
import kotlinx.io.files.*
import kotlin.test.*
import kotlin.time.*

class SimpleTest {

    @Test
    fun testSingle() {
        val tempPath = Path("build/foreign-temp")
        listOf(
            ClangTarget.MacosArm64,
            ClangTarget.MacosX64,
            ClangTarget.MingwX64,
            ClangTarget.LinuxX64,
            ClangTarget.IosDeviceArm64,
            ClangTarget.IosSimulatorArm64,
            ClangTarget.IosSimulatorX64,
        ).forEach { target ->
            val index = ClangCompiler.buildIndex(
                headers = setOf("openssl/bn.h"),
                compilerArgs = openssl3CompilerArgs(target)
            )
            val filteredIndex = index.filter(
                includedHeaderPatterns = listOf(Regex("openssl/bn\\.h"))
            )
            encode(Path(tempPath, "$target/bn/index.json"), index)
            encode(Path(tempPath, "$target/bn/index.filtered.json"), filteredIndex)
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
                ClangCompiler.buildIndex(
                    headers = setOf(header),
                    compilerArgs = openssl3CompilerArgs(target)
                )
            }
            println("[$indexP/${runs.size}] $targetP | $headerP : $time")
            assertTrue(
                result.variables.isNotEmpty() || // TODO?
                        result.enums.isNotEmpty() ||
                        result.records.isNotEmpty() ||
                        result.functions.isNotEmpty() ||
                        result.typedefs.isNotEmpty()
            )
        }
    }
}
