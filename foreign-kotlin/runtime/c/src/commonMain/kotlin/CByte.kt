@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*

// TODO: decide on Byte vs CByte (UByte vs CUByte) naming for all numbers

public inline var CPointer<Byte>.pointed: Byte
    get() = memoryBlock.getByte(0.toMemorySizeInt()) // mapper.getByte(block, offset)
    set(value) = memoryBlock.setByte(0.toMemorySizeInt(), value) // mapper.setByte(block, offset, value)

public operator fun CPointer.Companion.invoke(type: Byte.Companion): CType<CPointer<Byte>> = CType.CPointer(CType.Byte)
public fun CType.Companion.CPointer(type: Byte.Companion): CType<CPointer<Byte>> = CPointer(Byte)

public inline fun MemoryScope.allocateCPointer(type: Byte.Companion): CPointer<Byte> = TODO()
public inline fun MemoryScope.allocateCPointer(type: Byte.Companion, pointed: Byte): CPointer<Byte> = TODO()
public inline fun MemoryScope.allocateCPointer(pointed: Byte): CPointer<Byte> = TODO()

public inline fun MemoryScope.allocateCArray(type: Byte.Companion, size: Int): CArray<Byte> = TODO()
public inline fun MemoryScope.allocateCArray(type: Byte.Companion, value: ByteArray): CArray<Byte> = TODO()
public inline fun MemoryScope.allocateCArray(value: ByteArray): CArray<Byte> = TODO()

public inline fun ByteArray.copyInto(
    destination: CArray<Byte>
): CArray<Byte> = TODO()

public inline fun CPointer<Byte>.ofUByte(): CPointer<UByte> = TODO()
public inline fun CArray<Byte>.ofUByte(): CArray<UByte> = TODO()

public inline fun CPointer<UByte>.ofByte(): CPointer<Byte> = TODO()
public inline fun CArray<UByte>.ofByte(): CArray<Byte> = TODO()

// TODO: design conversions
public fun CArray<Byte>.copyInto(
    destination: ByteArray,
    destinationOffset: Int = 0,
    startIndex: Int = 0,
    endIndex: Int = size
): ByteArray = TODO()

// naming: toByteArray(), toByteArray(size), toByteArray(fromIndex, toIndex)
public fun CArray<Byte>.copyOfRange(fromIndex: Int, toIndex: Int): ByteArray = TODO()
public fun CArray<Byte>.copyOf(): ByteArray = TODO()
public fun CArray<Byte>.copyOf(size: Int): ByteArray = TODO()
