package dev.whyoleg.ffi.c

import kotlinx.cinterop.*

public val CPointer<*>?.nativeAddress: Long get() = this?.pointer?.value ?: 0

public fun nativeCString(pointer: kotlinx.cinterop.CPointer<ByteVar>?): CString? //= nativeCPointer(ByteVariableType, nativeAddress)
