@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*
import kotlin.wasm.*

actual fun ERR_get_error(): PlatformUInt = libCrypto3ImplicitScope.unsafe {
    ffi_ERR_get_error().toUInt()
}

actual fun ERR_error_string(
    e: PlatformUInt,
    buf: CString?,
): CString? = libCrypto3ImplicitScope.unsafe {
    CPointer(CType.Byte, ffi_ERR_error_string(e.toInt(), buf.address))
}

@WasmImport("foreign-crypto-wasm", "ffi_ERR_get_error")
private external fun ffi_ERR_get_error(): PlatformInt

@WasmImport("foreign-crypto-wasm", "ffi_ERR_error_string")
private external fun ffi_ERR_error_string(e: PlatformInt, buf: MemoryAddress): MemoryAddress
