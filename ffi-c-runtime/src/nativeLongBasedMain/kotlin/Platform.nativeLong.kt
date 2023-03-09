package dev.whyoleg.ffi.c

import kotlinx.cinterop.convert as kxc

public actual typealias PlatformInt = Long
public actual typealias CPlatformInt = CLong

public actual typealias PlatformUInt = ULong
public actual typealias CPlatformUInt = CULong

public actual inline fun <reified R : Any> PlatformInt.convert(): R = kxc()
public actual inline fun <reified R : Any> PlatformUInt.convert(): R = kxc()
