package dev.whyoleg.foreign.memory

import dev.whyoleg.foreign.platform.*

@ForeignMemoryApi
public actual inline fun MemoryBlock.loadPlatformInt(offset: MemoryAddressSize): PlatformInt = loadInt(offset)

@ForeignMemoryApi
public actual inline fun MemoryBlock.storePlatformInt(offset: MemoryAddressSize, value: PlatformInt): Unit = storeInt(offset, value)
