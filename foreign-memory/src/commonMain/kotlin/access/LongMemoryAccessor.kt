package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class LongMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<Long>(offset) {
    override val layout: MemoryLayout get() = MemoryLayout.Long
    override fun get(segment: MemorySegment): Long = getRaw(segment)
    override fun set(segment: MemorySegment, value: Long?): Unit = setRaw(segment, value ?: 0)
    override fun at(offset: MemoryAddressSize): MemoryAccessor<Long> = LongMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<Long>.getRaw(segment: MemorySegment): Long = segment.loadLong(offset)

@ForeignMemoryApi
public inline fun MemoryAccessor<Long>.setRaw(segment: MemorySegment, value: Long): Unit = segment.storeLong(offset, value)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Long>.getValue(thisRef: MemoryHolder, property: KProperty<*>): Long =
    getRaw(thisRef.segment)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Long>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: Long): Unit =
    setRaw(thisRef.segment, value)
