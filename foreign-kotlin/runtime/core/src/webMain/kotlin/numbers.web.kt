@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign

public actual typealias MemorySizeInt = Int

public actual inline fun Int.toMemorySizeInt(): MemorySizeInt = this
public actual inline fun Long.toMemorySizeInt(): MemorySizeInt = toInt() // TODO: overflow

public actual typealias PlatformInt = Int

public actual inline fun Int.toPlatformInt(): PlatformInt = this
public actual inline fun Long.toPlatformInt(): PlatformInt = toInt() // TODO: overflow

public actual typealias PlatformUInt = UInt

public actual inline fun UInt.toPlatformUInt(): PlatformUInt = this
public actual inline fun ULong.toPlatformUInt(): PlatformUInt = toUInt() // TODO: overflow
