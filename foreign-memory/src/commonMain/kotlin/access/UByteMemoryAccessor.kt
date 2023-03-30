package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class UByteMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<UByte>(offset) {
    override val layout: MemoryLayout get() = MemoryLayout.Byte
    override fun get(segment: MemorySegment): UByte = getRaw(segment)
    override fun set(segment: MemorySegment, value: UByte?): Unit = setRaw(segment, value ?: 0U)
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<UByte> = UByteMemoryAccessor(offset)
}

// all those re-declarations are needed to overcome boxing...
@ForeignMemoryApi
public inline fun MemoryAccessor<UByte>.getRaw(segment: MemorySegment): UByte = segment.loadByte(offset).toUByte()

@ForeignMemoryApi
public inline fun MemoryAccessor<UByte>.setRaw(segment: MemorySegment, value: UByte): Unit = segment.storeByte(offset, value.toByte())

@ForeignMemoryApi
public inline operator fun MemoryAccessor<UByte>.getValue(thisRef: MemoryHolder, property: KProperty<*>): UByte =
    getRaw(thisRef.segmentInternal)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<UByte>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: UByte): Unit =
    setRaw(thisRef.segmentInternal, value)
