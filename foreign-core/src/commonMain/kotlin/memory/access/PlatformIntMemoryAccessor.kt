package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class PlatformIntMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<PlatformInt>(offset) {
    override val layout: MemoryLayout get() = MemoryLayout.PlatformInt
    override fun get(segment: MemorySegment): PlatformInt = getRaw(segment)
    override fun set(segment: MemorySegment, value: PlatformInt?): Unit = setRaw(segment, value ?: 0.toPlatformInt())
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<PlatformInt> = PlatformIntMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<out PlatformInt>.getRaw(segment: MemorySegment): PlatformInt =
    segment.loadPlatformInt(offset)

@ForeignMemoryApi
public inline fun MemoryAccessor<out PlatformInt>.setRaw(segment: MemorySegment, value: PlatformInt): Unit =
    segment.storePlatformInt(offset, value)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<out PlatformInt>.getValue(
    thisRef: MemoryHolder,
    property: KProperty<*>
): PlatformInt = getRaw(thisRef.segmentInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<out PlatformInt>.setValue(
    thisRef: MemoryHolder,
    property: KProperty<*>,
    value: PlatformInt
): Unit = setRaw(thisRef.segmentInternal, value)
