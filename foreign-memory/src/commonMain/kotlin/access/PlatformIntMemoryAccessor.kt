package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*
import kotlin.reflect.*

@ForeignMemoryApi
internal expect class PlatformIntMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<PlatformInt>

@ForeignMemoryApi
public inline fun MemoryAccessor<PlatformInt>.getRaw(segment: MemorySegment): PlatformInt =
    segment.loadPlatformInt(offset)

@ForeignMemoryApi
public inline fun MemoryAccessor<PlatformInt>.setRaw(segment: MemorySegment, value: PlatformInt): Unit =
    segment.storePlatformInt(offset, value)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<PlatformInt>.getValue(thisRef: MemoryHolder, property: KProperty<*>): PlatformInt =
    getRaw(thisRef.segment)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<PlatformInt>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: PlatformInt): Unit =
    setRaw(thisRef.segment, value)
