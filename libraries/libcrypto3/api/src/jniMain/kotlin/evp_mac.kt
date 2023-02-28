@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual object EVP_MAC_Type : COpaqueType<EVP_MAC>(::EVP_MAC)
actual class EVP_MAC(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_MAC_Type get() = EVP_MAC_Type
}

actual object EVP_MAC_CTX_Type : COpaqueType<EVP_MAC_CTX>(::EVP_MAC_CTX)
actual class EVP_MAC_CTX(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_MAC_CTX_Type get() = EVP_MAC_CTX_Type
}

actual fun EVP_MAC_fetch(
    libctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MAC>? = CPointer(
    NativePointer(
        evpmac.EVP_MAC_fetch(libctx.nativePointer, algorithm.nativePointer, properties.nativePointer)
    ), EVP_MAC_Type
)

actual fun EVP_MAC_CTX_new(mac: CPointer<EVP_MAC>?): CPointer<EVP_MAC_CTX>? {
    return CPointer(NativePointer(evpmac.EVP_MAC_CTX_new(mac.nativePointer)), EVP_MAC_CTX_Type)
}

actual fun EVP_MAC_init(
    ctx: CPointer<EVP_MAC_CTX>?,
    key: CPointer<UByteVariable>?,
    keylen: ULong,
    params: CPointer<OSSL_PARAM>?,
): Int {
    return evpmac.EVP_MAC_init(ctx.nativePointer, key.nativePointer, keylen.toLong(), params.nativePointer)
}

actual fun EVP_MAC_CTX_get_mac_size(
    ctx: CPointer<EVP_MAC_CTX>?,
): ULong {
    return evpmac.EVP_MAC_CTX_get_mac_size(ctx.nativePointer).toULong()
}

actual fun EVP_MAC_update(
    ctx: CPointer<EVP_MAC_CTX>?,
    data: CPointer<UByteVariable>?,
    datalen: ULong,
): Int {
    return evpmac.EVP_MAC_update(ctx.nativePointer, data.nativePointer, datalen.toLong())
}

actual fun EVP_MAC_final(
    ctx: CPointer<EVP_MAC_CTX>?,
    out: CPointer<UByteVariable>?,
    outl: CPointer<ULongVariable>?,
    outsize: ULong,
): Int {
    return evpmac.EVP_MAC_final(ctx.nativePointer, out.nativePointer, outl.nativePointer, outsize.toLong())
}

actual fun EVP_MAC_CTX_free(ctx: CPointer<EVP_MAC_CTX>?) {
    evpmac.EVP_MAC_CTX_free(ctx.nativePointer)
}

actual fun EVP_MAC_free(ctx: CPointer<EVP_MAC>?) {
    evpmac.EVP_MAC_free(ctx.nativePointer)
}

private object evpmac {
    init {
        JNI
    }

    @JvmStatic
    external fun EVP_MAC_fetch(libctx: Long, algorithm: Long, properties: Long): Long

    @JvmStatic
    external fun EVP_MAC_free(ctx: Long)

    @JvmStatic
    external fun EVP_MAC_init(ctx: Long, key: Long, keylen: Long, params: Long): Int

    @JvmStatic
    external fun EVP_MAC_update(ctx: Long, data: Long, datalen: Long): Int

    @JvmStatic
    external fun EVP_MAC_final(ctx: Long, out: Long, outl: Long, outsize: Long): Int

    @JvmStatic
    external fun EVP_MAC_CTX_new(mac: Long): Long

    @JvmStatic
    external fun EVP_MAC_CTX_free(ctx: Long)

    @JvmStatic
    external fun EVP_MAC_CTX_get_mac_size(ctx: Long): Long
}
