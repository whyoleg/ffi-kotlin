@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*
import kotlinx.cinterop.*

actual fun ERR_get_error(): PlatformUInt = dev.whyoleg.ffi.libcrypto3.cinterop.ERR_get_error().convert()

actual fun ERR_error_string(
    e: PlatformUInt,
    buf: CString?,
): CString? = dev.whyoleg.ffi.libcrypto3.cinterop.ERR_error_string(e.convert(), buf)
