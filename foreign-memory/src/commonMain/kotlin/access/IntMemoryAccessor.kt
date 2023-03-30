package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class IntMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<Int>(offset) {
    override val layout: MemoryLayout get() = MemoryLayout.Int
    override fun get(segment: MemorySegment): Int = getRaw(segment)
    override fun set(segment: MemorySegment, value: Int?): Unit = setRaw(segment, value ?: 0)
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<Int> = IntMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<Int>.getRaw(segment: MemorySegment): Int = segment.loadInt(offset)

@ForeignMemoryApi
public inline fun MemoryAccessor<Int>.setRaw(segment: MemorySegment, value: Int): Unit = segment.storeInt(offset, value)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Int>.getValue(thisRef: MemoryHolder, property: KProperty<*>): Int =
    getRaw(thisRef.segmentInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Int>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: Int): Unit =
    setRaw(thisRef.segmentInternal, value)
