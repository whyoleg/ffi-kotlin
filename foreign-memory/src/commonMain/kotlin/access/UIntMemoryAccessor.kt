package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class UIntMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<UInt>(offset) {
    override val layout: MemoryLayout get() = MemoryLayout.Int
    override fun get(segment: MemorySegment): UInt = getRaw(segment)
    override fun set(segment: MemorySegment, value: UInt?): Unit = setRaw(segment, value ?: 0U)
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<UInt> = UIntMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<UInt>.getRaw(segment: MemorySegment): UInt = segment.loadInt(offset).toUInt()

@ForeignMemoryApi
public inline fun MemoryAccessor<UInt>.setRaw(segment: MemorySegment, value: UInt): Unit = segment.storeInt(offset, value.toInt())

@ForeignMemoryApi
public inline operator fun MemoryAccessor<UInt>.getValue(thisRef: MemoryHolder, property: KProperty<*>): UInt =
    getRaw(thisRef.segmentInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<UInt>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: UInt): Unit =
    setRaw(thisRef.segmentInternal, value)
