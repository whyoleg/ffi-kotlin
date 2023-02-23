package dev.whyoleg.ffi

import java.lang.foreign.*

public actual fun CString.toKString(): String = segment.getUtf8String(0)

public fun CString(segment: MemorySegment): CString? = CPointer(segment, CByteVariableType)
