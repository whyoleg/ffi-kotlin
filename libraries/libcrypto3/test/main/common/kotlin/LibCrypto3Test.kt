package dev.whyoleg.ffi.libcrypto3.test

import dev.whyoleg.ffi.*
import dev.whyoleg.ffi.libcrypto3.*
import kotlin.test.*

abstract class LibCrypto3Test {

    @Test
    fun testVersion() {
        val version = OpenSSL_version(OPENSSL_VERSION_STRING)?.toKString()
        println(version)
        assertEquals(3, version!!.first().digitToInt())
        assertEquals(3, OPENSSL_version_major().toInt())
    }

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


    @Test
    fun testEcdsa() {
        val dataInput = "Hi There".encodeToByteArray()

        val pkey = cInteropScope {
            val context = checkNotNull(EVP_PKEY_CTX_new_from_name(null, alloc("EC"), null))
            try {
                checkError(EVP_PKEY_keygen_init(context))
                checkError(
                    EVP_PKEY_CTX_set_params(
                        context, allocArrayOf(
                            OSSL_PARAM_Type,
                            OSSL_PARAM_construct_utf8_string(alloc("group"), alloc("P-521"), 0UL),
                            OSSL_PARAM_construct_end()
                        )
                    )
                )
                val pkeyVar = allocPointerTo<EVP_PKEY>()
                checkError(EVP_PKEY_generate(context, pkeyVar.pointer))
                checkNotNull(pkeyVar.value)
            } finally {
                EVP_PKEY_CTX_free(context)
            }
        }

        checkError(EVP_PKEY_up_ref(pkey))

        val signatureInput = cInteropScope {
            val context = checkNotNull(EVP_MD_CTX_new())
            try {
                checkError(
                    EVP_DigestSignInit_ex(
                        ctx = context,
                        pctx = null,
                        mdname = alloc("SHA256"),
                        libctx = null,
                        props = null,
                        pkey = pkey,
                        params = null
                    )
                )

                dataInput.read { dataPointer, dataSize ->
                    checkError(EVP_DigestSignUpdate(context, dataPointer, dataSize.toULong()))

                    val siglen = alloc(CULongVariableType)
                    checkError(EVP_DigestSignFinal(context, null, siglen.pointer))
                    println(siglen.value)
                    val signature = ByteArray(siglen.value.toInt())
                    signature.write { signaturePointer, signatureSize ->
                        checkError(EVP_DigestSignFinal(context, signaturePointer.toUByte(), siglen.pointer))
                    }
                    println(siglen.value)
                    signature.copyOf(siglen.value.toInt())
                }
            } finally {
                EVP_MD_CTX_free(context)
            }
        }

        EVP_PKEY_free(pkey)

        val result = cInteropScope {
            val context = checkNotNull(EVP_MD_CTX_new())
            try {
                checkError(
                    EVP_DigestVerifyInit_ex(
                        ctx = context,
                        pctx = null,
                        mdname = alloc("SHA256"),
                        libctx = null,
                        props = null,
                        pkey = pkey,
                        params = null
                    )
                )
                dataInput.read { dataPointer, dataSize ->
                    checkError(EVP_DigestVerifyUpdate(context, dataPointer, dataSize.toULong()))

                    val result = signatureInput.read { signaturePointer, signatureSize ->
                        EVP_DigestVerifyFinal(context, signaturePointer.toUByte(), signatureSize.toULong())
                    }
                    // 0     - means verification failed
                    // 1     - means verification succeeded
                    // other - means error
                    if (result != 0) checkError(result)
                    result == 1
                }
            } finally {
                EVP_MD_CTX_free(context)
            }
        }

        EVP_PKEY_free(pkey)

        assertTrue(EVP_PKEY_up_ref(pkey) == 0)

        assertTrue(result)
    }
}
