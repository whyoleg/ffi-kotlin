@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual fun ERR_get_error(): PlatformDependentUInt = err.ERR_get_error().toULong()

actual fun ERR_error_string(
    e: PlatformDependentUInt,
    buf: CString?,
): CString? = CString(NativePointer(err.ERR_error_string(e.toLong(), buf.nativePointer)))

private object err {
    init {
        JNI
    }

    @JvmStatic
    external fun ERR_get_error(): Long

    @JvmStatic
    external fun ERR_error_string(e: Long, buf: Long): Long
}
