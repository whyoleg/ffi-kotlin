@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*

actual fun ERR_get_error(): PlatformUInt = libCrypto3ImplicitScope.unsafe {
    err.ERR_get_error().toULong()
}

actual fun ERR_error_string(
    e: PlatformUInt,
    buf: CString?,
): CString? = libCrypto3ImplicitScope.unsafe {
    CPointer(CType.Byte, err.ERR_error_string(e.toLong(), buf.address))
}

private object err {
    @JvmStatic
    external fun ERR_get_error(): Long

    @JvmStatic
    external fun ERR_error_string(e: Long, buf: Long): Long
}
