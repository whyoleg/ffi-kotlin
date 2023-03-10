@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*
import java.lang.foreign.*
import java.lang.invoke.*

private val ERR_get_error_MH: MethodHandle = FFI.methodHandle(
    name = "ERR_get_error",
    result = ValueLayout.JAVA_LONG
)

actual fun ERR_get_error(): PlatformDependentUInt = (ERR_get_error_MH.invokeExact() as Long).toULong()

private val ERR_error_string_MH: MethodHandle = FFI.methodHandle(
    name = "ERR_error_string",
    result = ValueLayout.ADDRESS.asUnbounded(), //TODO: looks like unbounded should be for every result
    args = arrayOf(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS)
)

actual fun ERR_error_string(
    e: PlatformDependentUInt,
    buf: CString?,
): CString? = nativeCString(ERR_error_string_MH.invokeExact(e.toLong(), buf.nativeAddress) as MemorySegment)
