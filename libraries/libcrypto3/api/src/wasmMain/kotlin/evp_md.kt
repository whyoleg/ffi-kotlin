@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*
import kotlin.wasm.*

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
        ffi_EVP_MD_fetch(
            ctx.nativePointer,
            algorithm.nativePointer,
            properties.nativePointer
        )
    ), EVP_MD_Type
)

actual fun EVP_MD_CTX_new(): CPointer<EVP_MD_CTX>? {
    return CPointer(NativePointer(ffi_EVP_MD_CTX_new()), EVP_MD_CTX_Type)
}

actual fun EVP_MD_get_size(md: CPointer<EVP_MD>?): Int {
    return ffi_EVP_MD_get_size(md.nativePointer)
}

actual fun EVP_DigestInit(
    ctx: CPointer<EVP_MD_CTX>?,
    type: CPointer<EVP_MD>?,
): Int {
    return ffi_EVP_DigestInit(ctx.nativePointer, type.nativePointer)
}

actual fun EVP_DigestUpdate(
    ctx: CPointer<EVP_MD_CTX>?,
    d: CPointer<*>?,
    cnt: PlatformDependentUInt,
): Int {
    return ffi_EVP_DigestUpdate(ctx.nativePointer, d.nativePointer, cnt.toInt())
}

actual fun EVP_DigestFinal(
    ctx: CPointer<EVP_MD_CTX>?,
    md: CPointer<UByteVariable>?,
    s: CPointer<UIntVariable>?,
): Int {
    return ffi_EVP_DigestFinal(ctx.nativePointer, md.nativePointer, s.nativePointer)
}

actual fun EVP_MD_CTX_free(ctx: CPointer<EVP_MD_CTX>?) {
    ffi_EVP_MD_CTX_free(ctx.nativePointer)
}

actual fun EVP_MD_free(ctx: CPointer<EVP_MD>?) {
    ffi_EVP_MD_free(ctx.nativePointer)
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
