package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
internal class ByteMemoryAccessor(offset: MemoryAddressSize) : MemoryAccessor<Byte>(offset) {
    override val layout: MemoryLayout get() = MemoryLayout.Byte
    override fun get(segment: MemorySegment): Byte = getRaw(segment)
    override fun set(segment: MemorySegment, value: Byte?): Unit = setRaw(segment, value ?: 0)
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<Byte> = ByteMemoryAccessor(offset)
}

// all those re-declarations are needed to overcome boxing...
@ForeignMemoryApi
public inline fun MemoryAccessor<Byte>.getRaw(segment: MemorySegment): Byte = segment.loadByte(offset)

@ForeignMemoryApi
public inline fun MemoryAccessor<Byte>.setRaw(segment: MemorySegment, value: Byte): Unit = segment.storeByte(offset, value)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Byte>.getValue(thisRef: MemoryHolder, property: KProperty<*>): Byte =
    getRaw(thisRef.segment)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Byte>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: Byte): Unit =
    setRaw(thisRef.segment, value)
