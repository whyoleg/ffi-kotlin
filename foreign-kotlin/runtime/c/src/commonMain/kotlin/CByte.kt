@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*

// bytes
public inline var CPointer<Byte>.value: Byte
    get() = block.getByte(offset) // mapper.getByte(block, offset)
    set(value) = block.setByte(offset, value) // mapper.setByte(block, offset, value)

@Deprecated("use get") // error
public var CArray<Byte>.value: Byte by CPointer<Byte>::value

public operator fun CPointer.Companion.invoke(type: Byte.Companion): CType<CPointer<Byte>?> = TODO()
//public operator fun CArray.Companion.invoke(type: Byte.Companion, size: Int): CType<CArray<Byte>?> = TODO()

public inline fun MemoryScope.allocate(type: Byte.Companion): CPointer<Byte> = TODO()
public inline fun MemoryScope.allocateFrom(type: Byte.Companion, value: Byte): CPointer<Byte> = TODO()
public inline fun MemoryScope.allocateFrom(value: Byte): CPointer<Byte> = TODO()

public inline fun MemoryScope.allocateArray(type: Byte.Companion, size: Int): CArray<Byte> = TODO()
public inline fun MemoryScope.allocateArrayFrom(type: Byte.Companion, value: ByteArray): CArray<Byte> = TODO()
public inline fun MemoryScope.allocateArrayFrom(value: ByteArray): CArray<Byte> = TODO()

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

public fun CArray<Byte>.copyOfRange(fromIndex: Int, toIndex: Int): ByteArray = TODO()
public fun CArray<Byte>.copyOf(): ByteArray = TODO()
public fun CArray<Byte>.copyOf(size: Int): ByteArray = TODO()
