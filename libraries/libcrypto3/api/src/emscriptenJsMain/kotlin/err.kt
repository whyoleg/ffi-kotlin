@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*

actual fun ERR_get_error(): PlatformUInt = libCrypto3ImplicitScope.unsafe {
    err.ERR_get_error().toUInt()
}

actual fun ERR_error_string(
    e: PlatformUInt,
    buf: CString?,
): CString? = libCrypto3ImplicitScope.unsafe {
    CPointer(CType.Byte, err.ERR_error_string(e.toInt(), buf.address))
}

@JsModule("foreign-crypto-wasm")
@JsNonModule
@JsName("Module")
private external object err {
    @JsName("_ffi_ERR_get_error")
    fun ERR_get_error(): Int

    @JsName("_ffi_ERR_error_string")
    fun ERR_error_string(e: Int, buf: Int): Int
}
