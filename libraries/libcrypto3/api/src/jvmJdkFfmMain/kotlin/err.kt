@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.invoke.*
import dev.whyoleg.foreign.platform.*
import java.lang.foreign.*
import java.lang.invoke.*

private val ERR_get_error_MH: MethodHandle = FFI.methodHandle(
    name = "ERR_get_error",
    result = ValueLayout.JAVA_LONG
)

actual fun ERR_get_error(): PlatformUInt = libCrypto3ImplicitScope.unsafe {
    (ERR_get_error_MH.invokeExact() as Long).toULong()
}

private val ERR_error_string_MH: MethodHandle = FFI.methodHandle(
    name = "ERR_error_string",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS)
)

actual fun ERR_error_string(
    e: PlatformUInt,
    buf: CString?,
): CString? = libCrypto3ImplicitScope.unsafe {
    val address = ERR_error_string_MH.invokeExact(e.toLong(), buf.address) as MemorySegment
    CPointer(CType.Byte, address)
}
