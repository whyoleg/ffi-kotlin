@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual fun ERR_get_error(): CULong = dev.whyoleg.ffi.libcrypto3.cinterop.ERR_get_error()

actual fun ERR_error_string(
    e: CULong,
    buf: CString?,
): CString? = dev.whyoleg.ffi.libcrypto3.cinterop.ERR_error_string(e, buf)
