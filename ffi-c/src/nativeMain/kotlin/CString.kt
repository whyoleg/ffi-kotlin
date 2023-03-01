package dev.whyoleg.ffi.c

import kotlinx.cinterop.toKString as kxtoKString

public actual fun CString.toKString(): String = kxtoKString()
