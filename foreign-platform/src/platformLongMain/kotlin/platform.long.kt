package dev.whyoleg.foreign.platform

public actual typealias PlatformInt = Long
public actual typealias PlatformUInt = ULong

public actual inline fun PlatformInt.toPlatformUInt(): PlatformUInt = toULong()
public actual inline fun PlatformUInt.toPlatformInt(): PlatformInt = toLong()
