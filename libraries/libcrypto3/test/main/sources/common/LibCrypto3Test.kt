package dev.whyoleg.ffi.libcrypto3.test

import dev.whyoleg.ffi.*
import dev.whyoleg.ffi.libcrypto3.*
import kotlin.test.*

abstract class LibCrypto3Test {

//    @Test
//    fun testVersion() {
//        assertEquals(3, OpenSSL_version(OPENSSL_VERSION_STRING)?.toKString()!!.first().digitToInt())
//        assertEquals(3, OPENSSL_version_major().toInt())
//    }

    @Test
    fun testSha(): Unit = cInteropScope {
        val md = checkNotNull(EVP_MD_fetch(null, alloc("SHA256"), null))
        val digest = try {
            val context = checkNotNull(EVP_MD_CTX_new())
            try {
                "Hello World".encodeToByteArray().read { dataPointer, dataSize ->
                    val digest = ByteArray(checkError(EVP_MD_get_size(md)))
                    digest.write { digestPointer, digestSize ->
                        checkError(EVP_DigestInit(context, md))
                        checkError(EVP_DigestUpdate(context, dataPointer, dataSize.toULong()))
                        checkError(EVP_DigestFinal(context, digestPointer.toUByte(), null))
                    }
                    digest
                }
            } finally {
                EVP_MD_CTX_free(context)
            }
        } finally {
            EVP_MD_free(md)
        }
        assertEquals("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e", printHexBinary(digest))
    }

    @Test
    fun testHmac(): Unit = cInteropScope {
        val mac = checkNotNull(EVP_MAC_fetch(null, alloc("HMAC"), null))
        val signature = try {
            val context = EVP_MAC_CTX_new(mac)
            try {
                parseHexBinary("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b").read { keyPointer, keySize ->
                    "Hi There".encodeToByteArray().read { dataPointer, dataSize ->
                        checkError(
                            EVP_MAC_init(
                                ctx = context,
                                key = keyPointer.toUByte(),
                                keylen = keySize.toULong(),
                                params = allocArrayOf(
                                    OSSL_PARAM_Type,
                                    OSSL_PARAM_construct_utf8_string(alloc("digest"), alloc("SHA256"), 0UL),
                                    OSSL_PARAM_construct_end()
                                )
                            )
                        )
                        val signature = ByteArray(checkError(EVP_MAC_CTX_get_mac_size(context).toInt()))
                        signature.write { signaturePointer, signatureSize ->
                            checkError(EVP_MAC_update(context, dataPointer.toUByte(), dataSize.toULong()))
                            val out = alloc(CULongVariableType)
                            checkError(EVP_MAC_final(context, signaturePointer.toUByte(), out.pointer, signatureSize.toULong()))
                            println(out.value)
                        }
                        signature
                    }
                }
            } finally {
                EVP_MAC_CTX_free(context)
            }
        } finally {
            EVP_MAC_free(mac)
        }
        assertEquals("b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7", printHexBinary(signature))
    }
}
