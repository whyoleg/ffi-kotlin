package dev.whyoleg.foreign.index.cx.generator

import dev.whyoleg.foreign.index.cx.*
import kotlin.test.*

class TestIndexGeneration {
    // just tests for stability
    @Test
    fun test() {
        repeat(100) {
            val index = CxIndex.generate(
                headers = setOf(
                    "openssl/evp.h",
                    "openssl/err.h",
                    "openssl/encoder.h",
                    "openssl/decoder.h",
                    "openssl/ec.h",
                ),
                includePaths = setOf(
                    "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/openssl/prebuilt/macos-arm64/include",
                    "/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/System/Library/Frameworks/Kernel.framework/Headers/",
                    "/Library/Developer/CommandLineTools/SDKs/MacOSX.sdk/usr/include",
                )
            )

            assertTrue(index.headers.isNotEmpty())
        }

//        val output = "/Users/whyoleg/projects/opensource/whyoleg/ffi-kotlin/build/foreign/libcrypto3.json"
//        FileSystem.SYSTEM.writeCxIndex(output.toPath(), index)
//        FileSystem.SYSTEM.writeCxIndexVerbose("$output-verbose".toPath(), index)
    }

    @Test
    fun testError() {
        assertFailsWith<IllegalStateException> {
            CxIndex.generate(
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
        }
    }
}
