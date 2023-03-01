@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual object EVP_MD_Type : COpaqueType<EVP_MD>(::EVP_MD)
actual class EVP_MD(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_MD_Type get() = EVP_MD_Type
}

actual object EVP_MD_CTX_Type : COpaqueType<EVP_MD_CTX>(::EVP_MD_CTX)
actual class EVP_MD_CTX(memory: NativeMemory) : COpaque(memory) {
    override val type: EVP_MD_CTX_Type get() = EVP_MD_CTX_Type
}

actual fun EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MD>? = CPointer(
    NativePointer(
        evpmd.EVP_MD_fetch(
            ctx.nativePointer,
            algorithm.nativePointer,
            properties.nativePointer
        )
    ), EVP_MD_Type
)

actual fun EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>? {
    return CPointer(NativePointer(evpmd.EVP_MD_CTX_new()), EVP_MD_CTX_Type)
}

actual fun EVP_MD_get_size(md: CPointer<EVP_MD>?): Int {
    return evpmd.EVP_MD_get_size(md.nativePointer)
}

actual fun EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int {
    return evpmd.EVP_DigestInit(ctx.nativePointer, type.nativePointer)
}

actual fun EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: PlatformDependentUInt,
): Int {
    return evpmd.EVP_DigestUpdate(ctx.nativePointer, d.nativePointer, cnt.toLong())
}

actual fun EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<UByteVariable>?,
    s: CPointer<UIntVariable>?,
): Int {
    return evpmd.EVP_DigestFinal(ctx.nativePointer, md.nativePointer, s.nativePointer)
}

actual fun EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?) {
    evpmd.EVP_MD_CTX_free(ctx.nativePointer)
}

actual fun EVP_MD_free(ctx: CPointer<EVP_MD>?) {
    evpmd.EVP_MD_free(ctx.nativePointer)
}

private object evpmd {
    init {
        JNI
    }

    @JvmStatic
    external fun EVP_MD_fetch(ctx: Long, algorithm: Long, properties: Long): Long

    @JvmStatic
    external fun EVP_MD_free(md: Long)

    @JvmStatic
    external fun EVP_MD_get_size(md: Long): Int

    @JvmStatic
    external fun EVP_MD_CTX_new(): Long

    @JvmStatic
    external fun EVP_MD_CTX_free(ctx: Long)

    @JvmStatic
    external fun EVP_DigestInit(ctx: Long, type: Long): Int

    @JvmStatic
    external fun EVP_DigestUpdate(ctx: Long, d: Long, cnt: Long): Int

    @JvmStatic
    external fun EVP_DigestFinal(ctx: Long, md: Long, s: Long): Int
}
