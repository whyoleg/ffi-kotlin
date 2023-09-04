package dev.whyoleg.foreign.cx.index.generator

import dev.whyoleg.foreign.cx.storage.*
import okio.*
import okio.ByteString.Companion.encodeUtf8
import okio.Path.Companion.toPath
import kotlin.test.*

class TestIndexGeneration {

    @Test
    fun hm() {
        val headerFilePath = createTemporaryHeadersFile(
            setOf(
                "openssl/evp.h"
            )
        )

        mapOf(
            "mingw-x64" to listOf(
                "-B/Users/whyoleg/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/usr/bin",
                "-target",
                "x86_64-pc-windows-gnu",
                "--sysroot=/Users/whyoleg/.konan/dependencies/msys2-mingw-w64-x86_64-2"
            ),
            "linux-x64" to listOf(
                "-B/Users/whyoleg/.konan/dependencies/apple-llvm-20200714-macos-aarch64-essentials/usr/bin",
                "--gcc-toolchain=/Users/whyoleg/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2",
                "-target",
                "x86_64-unknown-linux-gnu",
                "--sysroot=/Users/whyoleg/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2/x86_64-unknown-linux-gnu/sysroot",
                // additional headers
                "-I/Users/whyoleg/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2/lib/gcc/x86_64-unknown-linux-gnu/8.3.0/include",
                "-I/Users/whyoleg/.konan/dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2/lib/gcc/x86_64-unknown-linux-gnu/8.3.0/include-fixed"
            ),
            "macos-arm64" to listOf(
                "-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin",
                "-target",
                "arm64-apple-macos10.16",
                "-isysroot",
                "/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX13.3.sdk",
                // additional headers
                "-I/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX13.3.sdk/System/Library/Frameworks/Kernel.framework/Headers",
            ),
            "ios-device-arm64" to listOf(
                "-B/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin",
                "-target",
                "arm64-apple-ios9.0",
                "-isysroot",
                "/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS16.4.sdk",
                // additional headers
                "-I/Applications/Xcode.app/Contents/Developer/Platforms/MacOSX.platform/Developer/SDKs/MacOSX13.3.sdk/System/Library/Frameworks/Kernel.framework/Headers",
            )
        ).forEach { (target, compilerArgs) ->
            println("RUN: $target")
            val result = CxIndexGenerator.generate(
                CxIndexGenerator.Arguments(
                    headerFilePath,
                    compilerArgs + listOf(
                        "-fno-stack-protector",
                        "-I/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/test-projects/libcrypto/api/build/tmp/setupOpenssl3/${target}/include",
                    )
                )
            )
            assertIs<CxIndexGenerator.Result.Success>(result, (result as? CxIndexGenerator.Result.Failure)?.message)
            val index = result.index
            val output = "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/libcrypto3-$target.json"
            SystemFileSystem.writeCxIndex(output.toPath(), index)
            SystemFileSystem.writeCxIndexVerbose("$output-verbose".toPath(), index)
        }
    }

    // just tests for stability
    //@Test
    fun test() {
        repeat(100) {
            val result = generate(
                headers = setOf(
                    "openssl/evp.h",
                    "openssl/err.h",
                    "openssl/encoder.h",
                    "openssl/decoder.h",
                    "openssl/ec.h",
                ),
                includePaths = setOf(
                    "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/test-projects/libcrypto/api/build/tmp/setupOpenssl3/macos-arm64/include",
                    "/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/System/Library/Frameworks/Kernel.framework/Headers/",
                    "/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/include",
                )
            )

            assertIs<CxIndexGenerator.Result.Success>(result)

            assertTrue(result.index.headers.isNotEmpty())
        }

//        val output = "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/libcrypto3.json"
//        SystemFileSystem.writeCxIndex(output.toPath(), index)
//        SystemFileSystem.writeCxIndexVerbose("$output-verbose".toPath(), index)
    }

    //@Test
    fun testError() {
        val result = generate(
            headers = setOf(
                "openssl/evp.h",
                "openssl/err.h",
                "openssl/encoder.h",
                "openssl/decoder.h",
                "openssl/ec.h",
            ),
            includePaths = setOf(
                "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/openssl/prebuilt/macos-arm64/include"
            )
        )
        assertIs<CxIndexGenerator.Result.Failure>(result)
        assertNotNull(result.message)
        assertTrue(result.message.startsWith("Indexing failed"), result.message)
    }
}

private fun generate(
    headers: Set<String>,
    includePaths: Set<String>
): CxIndexGenerator.Result = CxIndexGenerator.generate(
    CxIndexGenerator.Arguments(
        headerFilePath = createTemporaryHeadersFile(headers = headers),
        compilerArgs = includePaths.map { "-I$it" }
    )
)

internal fun createTemporaryHeadersFile(headers: Set<String>): String {
    val fileInfo = headers.joinToString("\n") { "#include <$it>" }
    val hashInfo = fileInfo.encodeUtf8().sha1().hex()
    val headersPath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("$hashInfo.h")
    val headersExists = SystemFileSystem.exists(headersPath)
    if (!headersExists) SystemFileSystem.write(headersPath, mustCreate = true) { writeUtf8(fileInfo) }
    return headersPath.toString()
}
