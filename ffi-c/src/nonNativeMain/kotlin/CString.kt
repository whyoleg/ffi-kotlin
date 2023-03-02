package dev.whyoleg.ffi.c

public actual fun CString.toKString(): String = pointed.memory.loadString(0)
