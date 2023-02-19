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
        val md = checkNotNull(EVP_MD_fetch(null, "SHA256", null))
        val digest = try {
            val context = checkNotNull(EVP_MD_CTX_new())
            try {
                "Hello World".encodeToByteArray().read { dataPointer, dataSize ->
                    val digest = ByteArray(check1(EVP_MD_get_size(md)))
                    digest.write { digestPointer, digestSize ->
                        check1(EVP_DigestInit(context, md))
                        check1(EVP_DigestUpdate(context, dataPointer, dataSize.toULong()))
                        check1(EVP_DigestFinal(context, digestPointer.toUByte(), null))
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

//    @Test
//    @OptIn(ExperimentalUnsignedTypes::class)
//    fun testHmac(): Unit = memScoped {
//        val dataInput = "Hi There".encodeToByteArray()
//        val hashAlgorithm = "SHA256"
//        val key = parseHexBinary("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b")
//
//        val mac = EVP_MAC_fetch(null, "HMAC", null)
//        val context = EVP_MAC_CTX_new(mac)
//        try {
//            checkError(
//                EVP_MAC_init(
//                    ctx = context,
//                    key = key.asUByteArray().refTo(0),
//                    keylen = key.size.convert(),
//                    params = OSSL_PARAM_array(
//                        OSSL_PARAM_construct_utf8_string("digest".cstr.ptr, hashAlgorithm.cstr.ptr, 0)
//                    )
//                )
//            )
//            val signature = ByteArray(EVP_MAC_CTX_get_mac_size(context).convert())
//
//            checkError(EVP_MAC_update(context, dataInput.fixEmpty().asUByteArray().refTo(0), dataInput.size.convert()))
//            checkError(EVP_MAC_final(context, signature.asUByteArray().refTo(0), null, signature.size.convert()))
//
//            assertEquals("b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7", printHexBinary(signature))
//        } finally {
//            EVP_MAC_CTX_free(context)
//            EVP_MAC_free(mac)
//        }
//    }
}

fun check1(value: Int): Int {
    check(value > 0)
    return value
}
