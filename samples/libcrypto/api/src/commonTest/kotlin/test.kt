package dev.whyoleg.foreign.samples.libcrypto

import dev.whyoleg.foreign.*
import dev.whyoleg.foreign.c.*

fun testSha(): Unit = AutoMemoryAccess().memoryScoped {
    val md = checkNotNull(
        EVP_MD_fetch(null, cPointerFrom("SHA256"), null, ::EVP_MD_free)
    )
    val context = checkNotNull(
        EVP_MD_CTX_new(::EVP_MD_CTX_free)
    )
    val dataArray = allocateCArray("Hello World".encodeToByteArray())
    val digestArray = allocateCArray(Byte, EVP_MD_get_size(md))

    EVP_DigestInit(context, md)
    EVP_DigestUpdate(context, dataArray, dataArray.size.toPlatformUInt())
    EVP_DigestFinal(context, digestArray.ofUByte(), null)

    val digest = digestArray.copyOf()
    assertEquals("a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e", printHexBinary(digest))
}
