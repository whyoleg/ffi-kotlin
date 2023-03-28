package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*
import kotlin.reflect.*

@ForeignMemoryApi
internal expect class PlatformUIntMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<PlatformUInt>

@ForeignMemoryApi
public inline fun MemoryAccessor<PlatformUInt>.getRaw(segment: MemorySegment): PlatformUInt =
    segment.loadPlatformInt(offset).toPlatformUInt()

@ForeignMemoryApi
public inline fun MemoryAccessor<PlatformUInt>.setRaw(segment: MemorySegment, value: PlatformUInt): Unit =
    segment.storePlatformInt(offset, value.toPlatformInt())

@ForeignMemoryApi
public inline operator fun MemoryAccessor<PlatformUInt>.getValue(thisRef: MemoryHolder, property: KProperty<*>): PlatformUInt =
    getRaw(thisRef.segment)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<PlatformUInt>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: PlatformUInt): Unit =
    setRaw(thisRef.segment, value)
