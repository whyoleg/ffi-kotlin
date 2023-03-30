@file:OptIn(ForeignMemoryApi::class)

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.jvm.*
import kotlin.reflect.*

// all those re-declarations are needed to overcome boxing...
@get:JvmName("getByteValue")
@set:JvmName("setByteValue")
public inline var CPointer<Byte>.pointed: Byte
    get() = accessor.getRaw(segmentInternal)
    set(value) = accessor.setRaw(segmentInternal, value)

@JvmName("getByteValue")
public inline operator fun CPointer<Byte>.getValue(thisRef: Any?, property: KProperty<*>): Byte = pointed

@JvmName("setByteValue")
public inline operator fun CPointer<Byte>.setValue(thisRef: Any?, property: KProperty<*>, value: Byte): Unit = run { this.pointed = value }

public inline fun MemoryScope.allocatePointerFor(type: CType.Byte, value: Byte): CPointer<Byte> =
    allocatePointerFor(type).apply { this.pointed = value }

public inline fun MemoryScope.allocatePointerFor(ignored: Byte.Companion, value: Byte = 0): CPointer<Byte> =
    allocatePointerFor(CType.Byte, value)

public inline fun MemoryScope.allocatePointer(value: Byte): CPointer<Byte> = allocatePointerFor(CType.Byte, value)

@JvmName("pointerByte")
public inline fun MemoryScope.allocatePointer(value: CPointer<Byte>?): CPointer<CPointer<Byte>> =
    allocatePointerFor(CType.Byte.pointer, value)

@get:JvmName("getIntValue")
@set:JvmName("setIntValue")
public inline var CPointer<Int>.pointed: Int
    get() = accessor.getRaw(segmentInternal)
    set(value) = accessor.setRaw(segmentInternal, value)

@JvmName("getIntValue")
public inline operator fun CPointer<Int>.getValue(thisRef: Any?, property: KProperty<*>): Int = pointed

@JvmName("setIntValue")
public inline operator fun CPointer<Int>.setValue(thisRef: Any?, property: KProperty<*>, value: Int): Unit = run { this.pointed = value }

public inline fun MemoryScope.allocatePointerFor(type: CType.Int, value: Int): CPointer<Int> =
    allocatePointerFor(type).apply { this.pointed = value }

public inline fun MemoryScope.allocatePointerFor(ignored: Int.Companion, value: Int = 0): CPointer<Int> =
    allocatePointerFor(CType.Int, value)

public inline fun MemoryScope.allocatePointer(value: Int): CPointer<Int> = allocatePointerFor(CType.Int, value)

@JvmName("pointerInt")
public inline fun MemoryScope.allocatePointer(value: CPointer<Int>?): CPointer<CPointer<Int>> = allocatePointerFor(CType.Int.pointer, value)

@get:JvmName("getUIntValue")
@set:JvmName("setUIntValue")
public inline var CPointer<UInt>.pointed: UInt
    get() = accessor.getRaw(segmentInternal)
    set(value) = accessor.setRaw(segmentInternal, value)

@JvmName("getUIntValue")
public inline operator fun CPointer<UInt>.getValue(thisRef: Any?, property: KProperty<*>): UInt = pointed

@JvmName("setUIntValue")
public inline operator fun CPointer<UInt>.setValue(thisRef: Any?, property: KProperty<*>, value: UInt): Unit = run { this.pointed = value }

public inline fun MemoryScope.allocatePointerFor(type: CType.UInt, value: UInt): CPointer<UInt> =
    allocatePointerFor(type).apply { this.pointed = value }

public inline fun MemoryScope.allocatePointerFor(ignored: UInt.Companion, value: UInt = 0U): CPointer<UInt> =
    allocatePointerFor(CType.UInt, value)

public inline fun MemoryScope.allocatePointer(value: UInt): CPointer<UInt> = allocatePointerFor(CType.UInt, value)

@JvmName("pointerUInt")
public inline fun MemoryScope.allocatePointer(value: CPointer<UInt>?): CPointer<CPointer<UInt>> =
    allocatePointerFor(CType.UInt.pointer, value)
