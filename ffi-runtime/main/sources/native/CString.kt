package dev.whyoleg.ffi

import kotlinx.cinterop.toKString as kxtoKString

public actual fun CString.toKString(): String = kxtoKString()
