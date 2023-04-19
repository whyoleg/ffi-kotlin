package dev.whyoleg.ffi.libcrypto3.test

import dev.whyoleg.ffi.libcrypto3.*
import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*
import kotlin.test.*

abstract class LibCrypto3Test {

    @Test
    fun testVersion() {
        val version = OpenSSL_version(OPENSSL_VERSION_STRING)?.toKString(unsafe = true)
        assertEquals(3, version!!.first().digitToInt())
        assertEquals(3, OPENSSL_version_major().toInt())
        assertEquals("3.0.8", version)
    }

    @Test
    fun testStrings(): Unit = foreignC {
        val ptr = cString("digest")

        assertEquals("digest", ptr.toKString(unsafe = true))
        assertEquals("digest", ptr.toKString(unsafe = false))
    }

    @Test
    fun testOsslParam(): Unit = foreignC {
        val param = OSSL_PARAM_construct_utf8_string(cString("digest"), cString("ALGORITHM"), 0.toPlatformUInt(), this)
        println(param)
        assertEquals(4U, param.data_type)
        assertEquals(9U, param.data_size.toUInt())
        assertEquals("digest", param.key?.toKString(unsafe = true))
        assertEquals("ALGORITHM", param.data?.reinterpret(CType.Byte)?.toKString(unsafe = true))
        param.data_type = 32U
        assertEquals(32U, param.data_type)
    }

    @Test
    fun testArrays(): Unit = foreignC {
        val of = cArrayOf(CType.Byte, 1, 2, 3)
        val of2 = cArrayOf(1, 2, 3)
        val dataArray = cArrayCopy(byteArrayOf(1, 2, 3))
        val digestArray = cArray(CType.Byte, 10)

        val osslArray = cArrayOf(
            OSSL_PARAM,
            OSSL_PARAM_construct_utf8_string(cString("digest"), cString("SHA256"), 0.toPlatformUInt(), this),
            OSSL_PARAM_construct_end(this)
        )
    }

    @Test
    fun testError(): Unit = foreignC {
        val mac = checkNotNull(
            EVP_MAC_fetch(null, cString("HMAC"), null).withCleanup(::EVP_MAC_free)
        )
        val context = checkNotNull(
            EVP_MAC_CTX_new(mac).withCleanup(::EVP_MAC_CTX_free)
        )
        val keyArray = cArrayCopy(ByteArray(1))
        val message = assertFailsWith<OpensslException> {
            checkError(
                EVP_MAC_init(
                    ctx = context,
                    key = keyArray.ofUByte(),
                    keylen = keyArray.size.toPlatformUInt(),
                    params = cArrayOf(
                        OSSL_PARAM,
                        OSSL_PARAM_construct_utf8_string(cString("digest"), cString("UNKNOWN"), 0.toPlatformUInt(), this),
                        OSSL_PARAM_construct_end(this)
                    )
                )
            )
        }.message
        assertEquals(
            "OPENSSL failure [result: 0, code: 50856204]: error:0308010C:digital envelope routines::unsupported",
            message
        )
    }

    @Test
    fun testSha(): Unit = foreignC {
        val md = checkNotNull(
            EVP_MD_fetch(null, cString("SHA256"), null).withCleanup(::EVP_MD_free)
        )
        val context = checkNotNull(
            EVP_MD_CTX_new().withCleanup(::EVP_MD_CTX_free)
        )
        val dataArray = cArrayCopy("Hello World".encodeToByteArray())
        val digestArray = cArray(CType.Byte, checkError(EVP_MD_get_size(md)))
        checkError(EVP_DigestInit(context, md))
        checkError(EVP_DigestUpdate(context, dataArray, dataArray.size.toPlatformUInt()))
        checkError(EVP_DigestFinal(context, digestArray.ofUByte(), null))
        val digest = digestArray.copyOf()
        assertEquals("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e", printHexBinary(digest))
    }

    @Test
    fun testHmac(): Unit = foreignC {
        val mac = checkNotNull(
            EVP_MAC_fetch(null, cString("HMAC"), null).withCleanup(::EVP_MAC_free)
        )
        val context = checkNotNull(
            EVP_MAC_CTX_new(mac).withCleanup(::EVP_MAC_CTX_free)
        )
        val keyArray = cArrayCopy(parseHexBinary("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b"))
        val dataArray = cArrayCopy("Hi There".encodeToByteArray())
        checkError(
            EVP_MAC_init(
                ctx = context,
                key = keyArray.ofUByte(),
                keylen = keyArray.size.toPlatformUInt(),
                params = cArrayOf(
                    OSSL_PARAM,
                    OSSL_PARAM_construct_utf8_string(cString("digest"), cString("SHA256"), 0.toPlatformUInt(), this),
                    OSSL_PARAM_construct_end(this)
                )
            )
        )
        val signatureArray = cArray(CType.Byte, checkError(EVP_MAC_CTX_get_mac_size(context).toInt()))
        assertEquals(32, signatureArray.size)
        checkError(EVP_MAC_update(context, dataArray.ofUByte(), dataArray.size.toPlatformUInt()))
        val out = cPointerOf(CType.PlatformUInt)
        checkError(EVP_MAC_final(context, signatureArray.ofUByte(), out, signatureArray.size.toPlatformUInt()))
        assertEquals(32, out.pointed.toInt())
        val signature = signatureArray.copyOf()
        assertEquals("b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7", printHexBinary(signature))
    }

    @Test
    fun testEcdsa() {
        val dataInput = "Hi There".encodeToByteArray()

        val pkey = foreignC {
            val context = checkNotNull(
                EVP_PKEY_CTX_new_from_name(null, cString("EC"), null).withCleanup(::EVP_PKEY_CTX_free)
            )
            checkError(EVP_PKEY_keygen_init(context))
            checkError(
                EVP_PKEY_CTX_set_params(
                    context, cArrayOf(
                        OSSL_PARAM,
                        OSSL_PARAM_construct_utf8_string(cString("group"), cString("P-521"), 0.toPlatformUInt(), this),
                        OSSL_PARAM_construct_end(this)
                    )
                )
            )
            val pkeyVar = cPointerOf(EVP_PKEY.pointer)
            checkError(EVP_PKEY_generate(context, pkeyVar))
            println(pkeyVar)
            println(pkeyVar.pointed)
            checkNotNull(pkeyVar.pointed)
        }

        checkError(EVP_PKEY_up_ref(pkey))

        val signatureInput = foreignC {
            val context = checkNotNull(
                EVP_MD_CTX_new().withCleanup(::EVP_MD_CTX_free)
            )
            checkError(
                EVP_DigestSignInit_ex(
                    ctx = context,
                    pctx = null,
                    mdname = cString("SHA256"),
                    libctx = null,
                    props = null,
                    pkey = pkey,
                    params = null
                )
            )

            val data = cArrayCopy(dataInput)
            checkError(EVP_DigestSignUpdate(context, data, data.size.toPlatformUInt()))

            val siglen = cPointerOf(CType.PlatformUInt)
            checkError(EVP_DigestSignFinal(context, null, siglen))
            assertContains(130..140, siglen.pointed.toInt())
            val signature = cArray(CType.Byte, siglen.pointed.toInt())
            checkError(EVP_DigestSignFinal(context, signature.ofUByte(), siglen))
            assertContains(130..140, siglen.pointed.toInt())
            //TODO: recheck indexes
            signature.copyOf(siglen.pointed.toInt())
        }

        EVP_PKEY_free(pkey)

        val result = foreignC {
            val context = checkNotNull(
                EVP_MD_CTX_new().withCleanup(::EVP_MD_CTX_free)
            )
            checkError(
                EVP_DigestVerifyInit_ex(
                    ctx = context,
                    pctx = null,
                    mdname = cString("SHA256"),
                    libctx = null,
                    props = null,
                    pkey = pkey,
                    params = null
                )
            )
            val data = cArrayCopy(dataInput)
            checkError(EVP_DigestVerifyUpdate(context, data, data.size.toPlatformUInt()))

            val signature = cArrayCopy(signatureInput)
            val result = EVP_DigestVerifyFinal(context, signature.ofUByte(), signature.size.toPlatformUInt())
            // 0     - means verification failed
            // 1     - means verification succeeded
            // other - means error
            if (result != 0) checkError(result)
            result == 1
        }

        EVP_PKEY_free(pkey)

        assertTrue(EVP_PKEY_up_ref(pkey) == 0)

        assertTrue(result)
    }
}
