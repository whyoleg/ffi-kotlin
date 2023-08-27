package dev.whyoleg.foreign.cx.index.generator

import okio.*
import okio.ByteString.Companion.encodeUtf8
import kotlin.test.*

class TestIndexGeneration {

    // just tests for stability
    @Test
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

    @Test
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
