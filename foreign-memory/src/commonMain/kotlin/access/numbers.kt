package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

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

@ForeignMemoryApi
public inline fun MemoryAccessor<Int>.getRaw(segment: MemorySegment): Int = segment.loadInt(offset)

@ForeignMemoryApi
public inline fun MemoryAccessor<Int>.setRaw(segment: MemorySegment, value: Int): Unit = segment.storeInt(offset, value)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Int>.getValue(thisRef: MemoryHolder, property: KProperty<*>): Int =
    getRaw(thisRef.segment)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<Int>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: Int): Unit =
    setRaw(thisRef.segment, value)

@ForeignMemoryApi
public inline fun MemoryAccessor<UInt>.getRaw(segment: MemorySegment): UInt = segment.loadInt(offset).toUInt()

@ForeignMemoryApi
public inline fun MemoryAccessor<UInt>.setRaw(segment: MemorySegment, value: UInt): Unit = segment.storeInt(offset, value.toInt())

@ForeignMemoryApi
public inline operator fun MemoryAccessor<UInt>.getValue(thisRef: MemoryHolder, property: KProperty<*>): UInt =
    getRaw(thisRef.segment)

@ForeignMemoryApi
public inline operator fun MemoryAccessor<UInt>.setValue(thisRef: MemoryHolder, property: KProperty<*>, value: UInt): Unit =
    setRaw(thisRef.segment, value)
