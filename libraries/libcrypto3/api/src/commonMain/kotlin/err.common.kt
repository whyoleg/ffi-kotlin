@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

expect fun ERR_get_error(): ULong

expect fun ERR_error_string(
    e: ULong,
    buf: CString?,
): CString?
