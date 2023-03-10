@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual fun ERR_get_error(): PlatformDependentUInt = err.ERR_get_error().toUInt()

actual fun ERR_error_string(
    e: PlatformDependentUInt,
    buf: CString?,
): CString? = nativeCString(err.ERR_error_string(e.toInt(), buf.nativeAddress))

@JsModule("ffi-libcrypto")
@JsNonModule
@JsName("Module")
private external object err {
    @JsName("_ffi_ERR_get_error")
    fun ERR_get_error(): Int

    @JsName("_ffi_ERR_error_string")
    fun ERR_error_string(e: Int, buf: Int): Int
}
