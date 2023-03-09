@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

expect fun ERR_get_error(): PlatformUInt

expect fun ERR_error_string(
    e: PlatformUInt,
    buf: CString?,
): CString?
