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

public inline fun MemoryScope.pointerFor(type: CType.Byte, value: Byte): CPointer<Byte> = pointerFor(type).apply { this.pointed = value }
public inline fun MemoryScope.pointerFor(ignored: Byte.Companion, value: Byte = 0): CPointer<Byte> = pointerFor(CType.Byte, value)
public inline fun MemoryScope.pointer(value: Byte): CPointer<Byte> = pointerFor(CType.Byte, value)

@JvmName("pointerByte")
public inline fun MemoryScope.pointer(value: CPointer<Byte>?): CPointer<CPointer<Byte>> = pointerFor(CType.Byte.pointer, value)

@get:JvmName("getIntValue")
@set:JvmName("setIntValue")
public inline var CPointer<Int>.pointed: Int
    get() = accessor.getRaw(segmentInternal)
    set(value) = accessor.setRaw(segmentInternal, value)

@JvmName("getIntValue")
public inline operator fun CPointer<Int>.getValue(thisRef: Any?, property: KProperty<*>): Int = pointed

@JvmName("setIntValue")
public inline operator fun CPointer<Int>.setValue(thisRef: Any?, property: KProperty<*>, value: Int): Unit = run { this.pointed = value }

public inline fun MemoryScope.pointerFor(type: CType.Int, value: Int): CPointer<Int> = pointerFor(type).apply { this.pointed = value }
public inline fun MemoryScope.pointerFor(ignored: Int.Companion, value: Int = 0): CPointer<Int> = pointerFor(CType.Int, value)
public inline fun MemoryScope.pointer(value: Int): CPointer<Int> = pointerFor(CType.Int, value)

@JvmName("pointerInt")
public inline fun MemoryScope.pointer(value: CPointer<Int>?): CPointer<CPointer<Int>> = pointerFor(CType.Int.pointer, value)

@get:JvmName("getUIntValue")
@set:JvmName("setUIntValue")
public inline var CPointer<UInt>.pointed: UInt
    get() = accessor.getRaw(segmentInternal)
    set(value) = accessor.setRaw(segmentInternal, value)

@JvmName("getUIntValue")
public inline operator fun CPointer<UInt>.getValue(thisRef: Any?, property: KProperty<*>): UInt = pointed

@JvmName("setUIntValue")
public inline operator fun CPointer<UInt>.setValue(thisRef: Any?, property: KProperty<*>, value: UInt): Unit = run { this.pointed = value }

public inline fun MemoryScope.pointerFor(type: CType.UInt, value: UInt): CPointer<UInt> = pointerFor(type).apply { this.pointed = value }
public inline fun MemoryScope.pointerFor(ignored: UInt.Companion, value: UInt = 0U): CPointer<UInt> = pointerFor(CType.UInt, value)
public inline fun MemoryScope.pointer(value: UInt): CPointer<UInt> = pointerFor(CType.UInt, value)

@JvmName("pointerUInt")
public inline fun MemoryScope.pointer(value: CPointer<UInt>?): CPointer<CPointer<UInt>> = pointerFor(CType.UInt.pointer, value)
