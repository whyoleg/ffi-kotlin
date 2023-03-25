package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.reflect.*

// all those re-declarations are needed to overcome boxing...
@OptIn(ForeignMemoryApi::class)
public inline var CPointer<Byte>.value: Byte
    get() = accessor.getRaw(segment)
    set(value) = accessor.setRaw(segment, value)

public inline operator fun CPointer<Byte>.getValue(thisRef: Any?, property: KProperty<*>): Byte = value
public inline operator fun CPointer<Byte>.setValue(thisRef: Any?, property: KProperty<*>, value: Byte): Unit = run { this.value = value }

public inline fun MemoryScope.pointerFor(type: CType.Byte, value: Byte): CPointer<Byte> = pointerFor(type).apply { this.value = value }
public inline fun MemoryScope.pointerFor(ignored: Byte.Companion, value: Byte = 0): CPointer<Byte> = pointerFor(CType.Byte, value)
public inline fun MemoryScope.pointer(value: Byte): CPointer<Byte> = pointerFor(CType.Byte, value)
public inline fun MemoryScope.pointer(value: CPointer<Byte>?): CPointer<CPointer<Byte>> = pointerFor(CType.Byte.pointer, value)

@OptIn(ForeignMemoryApi::class)
public inline var CPointer<Int>.value: Int
    get() = accessor.getRaw(segment)
    set(value) = accessor.setRaw(segment, value)

public inline operator fun CPointer<Int>.getValue(thisRef: Any?, property: KProperty<*>): Int = value
public inline operator fun CPointer<Int>.setValue(thisRef: Any?, property: KProperty<*>, value: Int): Unit = run { this.value = value }

public inline fun MemoryScope.pointerFor(type: CType.Int, value: Int): CPointer<Int> = pointerFor(type).apply { this.value = value }
public inline fun MemoryScope.pointerFor(ignored: Int.Companion, value: Int = 0): CPointer<Int> = pointerFor(CType.Int, value)
public inline fun MemoryScope.pointer(value: Int): CPointer<Int> = pointerFor(CType.Int, value)
public inline fun MemoryScope.pointer(value: CPointer<Int>?): CPointer<CPointer<Int>> = pointerFor(CType.Int.pointer, value)

@OptIn(ForeignMemoryApi::class)
public inline var CPointer<UInt>.value: UInt
    get() = accessor.getRaw(segment)
    set(value) = accessor.setRaw(segment, value)

public inline operator fun CPointer<UInt>.getValue(thisRef: Any?, property: KProperty<*>): UInt = value
public inline operator fun CPointer<UInt>.setValue(thisRef: Any?, property: KProperty<*>, value: UInt): Unit = run { this.value = value }

public inline fun MemoryScope.pointerFor(type: CType.UInt, value: UInt): CPointer<UInt> = pointerFor(type).apply { this.value = value }
public inline fun MemoryScope.pointerFor(ignored: UInt.Companion, value: UInt = 0U): CPointer<UInt> = pointerFor(CType.UInt, value)
public inline fun MemoryScope.pointer(value: UInt): CPointer<UInt> = pointerFor(CType.UInt, value)
public inline fun MemoryScope.pointer(value: CPointer<UInt>?): CPointer<CPointer<UInt>> = pointerFor(CType.UInt.pointer, value)
