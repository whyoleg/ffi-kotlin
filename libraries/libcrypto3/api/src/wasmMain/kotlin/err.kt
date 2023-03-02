@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import kotlin.wasm.*

actual fun ERR_get_error(): PlatformDependentUInt = ffi_ERR_get_error().toUInt()

actual fun ERR_error_string(
    e: PlatformDependentUInt,
    buf: CString?,
): CString? = nativeCString(ffi_ERR_error_string(e.toInt(), buf.nativeAddress))

@WasmImport("ffi-libcrypto", "ffi_ERR_get_error")
private external fun ffi_ERR_get_error(): Int

@WasmImport("ffi-libcrypto", "ffi_ERR_error_string")
private external fun ffi_ERR_error_string(e: Int, buf: Int): Int
