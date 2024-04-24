@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.invoke.*
import java.lang.foreign.*
import java.lang.invoke.*

actual const val OPENSSL_VERSION_STRING: Int = 6 //magic :)

private val OpenSSL_version: MethodHandle = FFI.methodHandle(
    name = "OpenSSL_version",
    result = ValueLayout.ADDRESS,
    args = arrayOf(ValueLayout.JAVA_INT)
)

actual fun OpenSSL_version(type: Int): CString? = libCrypto3ImplicitScope.unsafe {
    return CPointer(CType.Byte, OpenSSL_version.invokeExact(type) as MemorySegment)
}

private val OPENSSL_version_major: MethodHandle = FFI.methodHandle(
    name = "OPENSSL_version_major",
    result = ValueLayout.JAVA_INT
)

actual fun OPENSSL_version_major(): UInt = libCrypto3ImplicitScope.unsafe {
    return (OPENSSL_version_major.invokeExact() as Int).toUInt()
}
