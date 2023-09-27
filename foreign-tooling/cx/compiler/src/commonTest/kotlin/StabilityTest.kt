package dev.whyoleg.foreign.tooling.cx.compiler

import dev.whyoleg.foreign.tooling.cx.compiler.model.*
import kotlin.test.*
import kotlin.time.*

class StabilityTest {
    @Test
    fun test() {

        val baseIncludePath = "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/test-projects/libcrypto/api/build/tmp/setupOpenssl3"

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
                        "ec.h",
                        "bio.h",
                        "evp.h",
                        "bn.h",
                        "aes.h",
                        "types.h",
                        "encoder.h",
                        "ssl.h",
                    ).forEach { header ->
                        add(target to header)
                    }
                }
            }
        }

        runs.shuffled().forEachIndexed { index, (target, header) ->
            val dirName = dirName(target)

            val (result, time) = measureTimedValue {
                CxCompiler.buildIndex(
                    mainFileName = "openssl/$header",
                    mainFilePath = "$baseIncludePath/$dirName/include/openssl/$header",
                    compilerArgs = compilerArgs(target) + listOf(
                        "-I$baseIncludePath/$dirName/include"
                    )
                )
            }
            val r = runs.size.toString()
            val i = index.toString().padStart(r.length)
            println("[$i/$r] $target/$header: $time")
            assertTrue(
                result.enums.isNotEmpty() ||
                        result.records.isNotEmpty() ||
                        result.functions.isNotEmpty() ||
                        result.typedefs.isNotEmpty()
            )
        }
    }
}
