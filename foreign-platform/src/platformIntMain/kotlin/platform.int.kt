package dev.whyoleg.foreign.platform

public actual typealias PlatformInt = Int
public actual typealias PlatformUInt = UInt

public actual inline fun PlatformInt.toPlatformUInt(): PlatformUInt = toUInt()
public actual inline fun PlatformUInt.toPlatformInt(): PlatformInt = toInt()

public actual inline fun Number.toPlatformInt(): PlatformInt = toInt()
public actual inline fun Number.toPlatformUInt(): PlatformUInt = toInt().toUInt()

