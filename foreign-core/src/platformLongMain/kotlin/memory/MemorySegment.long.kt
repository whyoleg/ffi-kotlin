package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.platform.*

@ForeignMemoryApi
public actual inline fun MemorySegment.loadPlatformInt(offset: MemoryAddressSize): PlatformInt = loadLong(offset)

@ForeignMemoryApi
public actual inline fun MemorySegment.storePlatformInt(offset: MemoryAddressSize, value: PlatformInt): Unit = storeLong(offset, value)
