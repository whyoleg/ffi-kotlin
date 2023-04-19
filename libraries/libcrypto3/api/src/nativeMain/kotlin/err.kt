@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*
import kotlinx.cinterop.internal.*

actual fun ERR_get_error(): PlatformUInt = libCrypto3ImplicitScope.unsafe {
    ffi_ERR_get_error().toPlatformUInt()
}

actual fun ERR_error_string(
    e: PlatformUInt,
    buf: CString?,
): CString? = libCrypto3ImplicitScope.unsafe {
    CPointer(CType.Byte, ffi_ERR_error_string(e.toLong(), buf.address.toLong()))
}


@CCall("ffi_ERR_get_error")
private external fun ffi_ERR_get_error(): Long

@CCall("ffi_ERR_error_string")
private external fun ffi_ERR_error_string(e: Long, buf: Long): Long
