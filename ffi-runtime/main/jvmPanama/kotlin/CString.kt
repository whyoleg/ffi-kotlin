package dev.whyoleg.ffi

public actual fun CString.toKString(): String = segment.getUtf8String(0)
