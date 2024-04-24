package dev.whyoleg.foreign.clang.compiler

import dev.whyoleg.foreign.clang.api.*
import dev.whyoleg.foreign.clang.arguments.*
import kotlinx.io.*
import kotlinx.io.files.*
import kotlin.test.*
import kotlin.time.*

class SimpleTest {

    private fun saveIndex(outputPath: String, index: CxIndex) {
        val indexString = CxIndex.encode(index)
        SystemFileSystem.sink(Path(outputPath)).buffered().use { output ->
            output.writeString(indexString)
        }
    }

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
            val index = ClangCompiler.buildIndex(
                headersPath = createHeadersFile(setOf("openssl/bn.h")),
                compilerArgs = openssl3CompilerArgs(target)
            )

            saveIndex(
                "/Users/Oleg.Yukhnevich/Projects/whyoleg/ffi-kotlin/foreign-kotlin/clang/compiler/build/index-$target.json",
                index
            )

            saveIndex(
                "/Users/Oleg.Yukhnevich/Projects/whyoleg/ffi-kotlin/foreign-kotlin/clang/compiler/build/index-$target.filtered.json",
                index.filter(
                    listOf(Regex("openssl/bn\\.h")),
                    emptyList()
                )
            )
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
                    headersPath = createHeadersFile(setOf(header)),
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
