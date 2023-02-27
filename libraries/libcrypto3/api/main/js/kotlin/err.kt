@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual fun ERR_get_error(): ULong = err.ERR_get_error().toULong()

actual fun ERR_error_string(
    e: ULong,
    buf: CString?,
): CString? = CString(NativePointer(err.ERR_error_string(e.toInt(), buf.nativePointer)))

@JsModule("ffi-libcrypto")
@JsNonModule
@JsName("Module")
private external object err {
    @JsName("_ffi_ERR_get_error")
    fun ERR_get_error(): Int

    @JsName("_ffi_ERR_error_string")
    fun ERR_error_string(e: Int, buf: Int): Int
}
