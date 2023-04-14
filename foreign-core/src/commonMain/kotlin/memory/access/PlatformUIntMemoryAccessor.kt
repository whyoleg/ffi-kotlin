package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class PlatformUIntMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<PlatformUInt>(offset) {
    override val layout: MemoryLayout get() = MemoryLayout.PlatformInt
    override fun get(segment: MemorySegment): PlatformUInt = getRaw(segment)
    override fun set(segment: MemorySegment, value: PlatformUInt?): Unit = setRaw(segment, value ?: 0.toPlatformUInt())
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<PlatformUInt> = PlatformUIntMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<out PlatformUInt>.getRaw(segment: MemorySegment): PlatformUInt =
    segment.loadPlatformInt(offset).toPlatformUInt()

@ForeignMemoryApi
public inline fun MemoryAccessor<out PlatformUInt>.setRaw(segment: MemorySegment, value: PlatformUInt): Unit =
    segment.storePlatformInt(offset, value.toPlatformInt())

@ForeignMemoryApi
public inline operator fun MemoryAccessor<out PlatformUInt>.getValue(
    thisRef: MemoryHolder,
    property: KProperty<*>
): PlatformUInt =
    getRaw(thisRef.segmentInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<out PlatformUInt>.setValue(
    thisRef: MemoryHolder,
    property: KProperty<*>,
    value: PlatformUInt
): Unit =
    setRaw(thisRef.segmentInternal, value)
