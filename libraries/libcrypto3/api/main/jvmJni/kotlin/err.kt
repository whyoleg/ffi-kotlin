@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual fun ERR_get_error(): CULong = TODO()

actual fun ERR_error_string(
    e: CULong,
    buf: CString?,
): CString? = TODO()
