@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import kotlin.wasm.*

actual fun EVP_MD_fetch(
    ctx: CPointer<OSSL_LIB_CTX>?,
    algorithm: CString?,
    properties: CString?,
): CPointer<EVP_MD>? = nativeCPointer(
    EVP_MD_Type, ffi_EVP_MD_fetch(ctx.nativeAddress, algorithm.nativeAddress, properties.nativeAddress)
)

actual fun EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>? {
    return nativeCPointer(EVP_MD_CTX_Type, ffi_EVP_MD_CTX_new())
}

actual fun EVP_MD_get_size(md: CPointer<EVP_MD>?): Int {
    return ffi_EVP_MD_get_size(md.nativeAddress)
}

actual fun EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int {
    return ffi_EVP_DigestInit(ctx.nativeAddress, type.nativeAddress)
}

actual fun EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: PlatformDependentUInt,
): Int {
    return ffi_EVP_DigestUpdate(ctx.nativeAddress, d.nativeAddress, cnt.toInt())
}

actual fun EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<UByteVariable>?,
    s: CPointer<UIntVariable>?,
): Int {
    return ffi_EVP_DigestFinal(ctx.nativeAddress, md.nativeAddress, s.nativeAddress)
}

actual fun EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?) {
    ffi_EVP_MD_CTX_free(ctx.nativeAddress)
}

actual fun EVP_MD_free(ctx: CPointer<EVP_MD>?) {
    ffi_EVP_MD_free(ctx.nativeAddress)
}

@WasmImport("ffi-libcrypto", "ffi_EVP_MD_fetch")
private external fun ffi_EVP_MD_fetch(ctx: Int, algorithm: Int, properties: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_MD_free")
private external fun ffi_EVP_MD_free(md: Int)

@WasmImport("ffi-libcrypto", "ffi_EVP_MD_get_size")
private external fun ffi_EVP_MD_get_size(md: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_MD_CTX_new")
private external fun ffi_EVP_MD_CTX_new(): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_MD_CTX_free")
private external fun ffi_EVP_MD_CTX_free(ctx: Int)

@WasmImport("ffi-libcrypto", "ffi_EVP_DigestInit")
private external fun ffi_EVP_DigestInit(ctx: Int, type: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_DigestUpdate")
private external fun ffi_EVP_DigestUpdate(ctx: Int, d: Int, cnt: Int): Int

@WasmImport("ffi-libcrypto", "ffi_EVP_DigestFinal")
private external fun ffi_EVP_DigestFinal(ctx: Int, md: Int, s: Int): Int
