@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign

public actual typealias PlatformInt = Long

public actual inline fun Int.toPlatformInt(): PlatformInt = toLong()
public actual inline fun Long.toPlatformInt(): PlatformInt = this

public actual typealias PlatformUInt = ULong

public actual inline fun UInt.toPlatformUInt(): PlatformUInt = toULong()
public actual inline fun ULong.toPlatformUInt(): PlatformUInt = this
