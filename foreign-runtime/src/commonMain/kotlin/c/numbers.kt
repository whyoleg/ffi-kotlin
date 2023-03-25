package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*

public inline fun MemoryScope.pointer(value: Byte = 0): CPointer<Byte> = pointerFor(Byte).apply { this.value = value }
public inline fun MemoryScope.pointerFor(ignored: Byte.Companion): CPointer<Byte> = pointerFor(CType.Byte)
public inline fun MemoryScope.pointerTo(value: CPointer<Byte>?): CPointer<CPointer<Byte>?> = pointerTo(CType.Byte, value)

public inline fun MemoryScope.pointer(value: Int = 0): CPointer<Int> = pointerFor(Int).apply { this.value = value }
public inline fun MemoryScope.pointerFor(ignored: Int.Companion): CPointer<Int> = pointerFor(CType.Int)
public inline fun MemoryScope.pointerTo(value: CPointer<Int>?): CPointer<CPointer<Int>?> = pointerTo(CType.Int, value)

public inline fun MemoryScope.pointer(value: Long = 0): CPointer<Long> = pointerFor(Long).apply { this.value = value }
public inline fun MemoryScope.pointerFor(ignored: Long.Companion): CPointer<Long> = pointerFor(CType.Long)
public inline fun MemoryScope.pointerTo(value: CPointer<Long>?): CPointer<CPointer<Long>?> = pointerTo(CType.Long, value)
