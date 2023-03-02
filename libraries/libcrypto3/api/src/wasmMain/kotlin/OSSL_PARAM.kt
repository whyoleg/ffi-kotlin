@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import kotlin.wasm.*

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformDependentUInt,
): CValue<OSSL_PARAM> = nativeCValue(OSSL_PARAM_Type) { nativeAddress ->
    ffi_OSSL_PARAM_construct_utf8_string(key.nativeAddress, buf.nativeAddress, bsize.toInt(), nativeAddress)
}

actual fun OSSL_PARAM_construct_end(): CValue<OSSL_PARAM> = nativeCValue(OSSL_PARAM_Type) { nativeAddress ->
    ffi_OSSL_PARAM_construct_end(nativeAddress)
}

@WasmImport("ffi-libcrypto", "ffi_OSSL_PARAM_construct_utf8_string")
private external fun ffi_OSSL_PARAM_construct_utf8_string(key: Int, buf: Int, bsize: Int, returnPointer: Int)

@WasmImport("ffi-libcrypto", "ffi_OSSL_PARAM_construct_end")
private external fun ffi_OSSL_PARAM_construct_end(returnPointer: Int)
