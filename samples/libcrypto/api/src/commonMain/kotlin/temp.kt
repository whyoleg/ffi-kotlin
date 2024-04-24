package dev.whyoleg.foreign.samples.libcrypto

import dev.whyoleg.foreign.*
import dev.whyoleg.foreign.c.*

// 3 types of functions:
//  1. returns `primitive` : fun NAME(args): PRIMITIVE (OPENSSL_version_major)
//  2. returns `pointer`   : context(MemoryScope) fun NAME(rags, cleanup: (POINTER) -> Unit = {}): POINTER (OpenSSL_version)
//  3. returns `struct`    : context(MemoryScope) fun NAME(rags): STRUCT (OSSL_PARAM_construct_end)


// opensslv

expect val OPENSSL_VERSION_STRING: Int

expect fun MemoryScope.OpenSSL_version(
    type: Int,
    cleanup: MemoryCleanupAction<CString?>? = null
): CString?

expect fun OPENSSL_version_major(): UInt

// ossl

expect class OSSL_PARAM : CStruct {
    var key: CString?
    var data_type: UInt
    var data: CPointer<Unit>?
    var data_size: PlatformUInt
    var return_size: PlatformUInt

    companion object : CType.Record<OSSL_PARAM>
}

expect inline fun MemoryScope.OSSL_PARAM(block: OSSL_PARAM.() -> Unit = {}): OSSL_PARAM

//@JvmInline
//value class OSSL_PARAM2 internal constructor(private val block: MemoryBlock) : CStruct<OSSL_PARAM> {
//    var key: CString?
//        get() = TODO()
//        set(value) = TODO()
//    var data_type: UInt
//        get() = block.getInt(OSSL_PARAM_offsets.data_type).toUInt()
//        set(value) = block.setInt(OSSL_PARAM_offsets.data_type, value.toInt())
//    var data: CPointer<Unit>?
//        get() = TODO()
//        set(value) = TODO()
//    var data_size: PlatformUInt
//        get() = TODO()
//        set(value) = TODO()
//    var return_size: PlatformUInt
//        get() = TODO()
//        set(value) = TODO()
//
//    companion object Type : CType.Record<OSSL_PARAM>
//}

expect fun MemoryScope.OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformUInt,
): OSSL_PARAM

expect fun MemoryScope.OSSL_PARAM_construct_end(): OSSL_PARAM

expect class OSSL_LIB_CTX : COpaque {
    companion object : CType.Opaque<OSSL_LIB_CTX>
}

object EVP_MD : COpaque, CType.Opaque<EVP_MD>

object EVP_MD_CTX : COpaque, CType.Opaque<EVP_MD_CTX>

// TODO: this should be `context(memoryScope: MemoryScope)` when it will be ready

expect fun MemoryScope.EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
    cleanup: MemoryCleanupAction<CPointer<EVP_MD>?>? = null
): CPointer<EVP_MD>?

expect fun MemoryScope.EVP_MD_CTX_new(
    cleanup: MemoryCleanupAction<CPointer<EVP_MD_CTX>?>? = null
): CPointer<EVP_MD_CTX>?

expect fun EVP_MD_get_size(md: CPointer<EVP_MD>?): Int

expect fun EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int

expect fun EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: PlatformUInt,
): Int

expect fun EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<UByte>?,
    s: CPointer<UInt>?,
): Int

expect fun EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?)

expect fun EVP_MD_free(ctx: CPointer<EVP_MD>?)
