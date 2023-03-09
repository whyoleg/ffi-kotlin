package dev.whyoleg.ffi.c

import kotlinx.cinterop.convert as kxc

public actual typealias PlatformInt = Int
public actual typealias CPlatformInt = CInt

public actual typealias PlatformUInt = UInt
public actual typealias CPlatformUInt = CUInt

public actual inline fun <reified R : Any> PlatformInt.convert(): R = kxc()
public actual inline fun <reified R : Any> PlatformUInt.convert(): R = kxc()
