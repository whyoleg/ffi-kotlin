package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class ULongMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<ULong>(offset) {
    override val layout: MemoryLayout get() = MemoryLayout.Long
    override fun get(segment: MemorySegment): ULong = getRaw(segment)
    override fun set(segment: MemorySegment, value: ULong?): Unit = setRaw(segment, value ?: 0U)
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<ULong> = ULongMemoryAccessor(offset)
}

@ForeignMemoryApi
public inline fun MemoryAccessor<ULong>.getRaw(segment: MemorySegment): ULong = segment.loadLong(offset).toULong()

@ForeignMemoryApi
public inline fun MemoryAccessor<ULong>.setRaw(segment: MemorySegment, value: ULong): Unit = segment.storeLong(offset, value.toLong())

@ForeignMemoryApi
public inline operator fun MemoryAccessor<ULong>.getValue(thisRef: MemoryHolder, property: KProperty<*>): ULong =
    getRaw(thisRef.segment)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<ULong>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: ULong): Unit =
    setRaw(thisRef.segment, value)
