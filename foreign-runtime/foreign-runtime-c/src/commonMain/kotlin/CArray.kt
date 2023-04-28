package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*

public typealias CArrayPointer<KT> = CPointer<KT>

@OptIn(ForeignMemoryApi::class)
public class CArray<KT : Any>
internal constructor(
    public val size: Int,
    accessor: MemoryAccessor<KT>,
    segment: MemoryBlock,
) : CArrayPointer<KT>(accessor, segment)

@OptIn(ForeignMemoryApi::class)
public operator fun <KT : Any> CArray<KT>.get(index: Int): CPointer<KT> {
    if (index == 0) return this
    check(index < size) { "Array out of bound access" }

    //TODO: check index ON access?
    return CPointer(
        accessor.at(accessor.offset + accessor.layout.size * index),
        blockInternalC
    )
}

@OptIn(ForeignMemoryApi::class)
public fun CArray<Byte>.ofUByte(): CArray<UByte> = CArray(size, MemoryAccessor.UByte.at(accessor.offset), blockInternalC)

// factory functions

@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> ForeignCScope.cArray(type: CType<KT>, size: Int): CArray<KT> = unsafe {
    CArray(size, type.accessor, arena.allocateArray(type.layout, size))
}

public fun <KT : Any> ForeignCScope.cArrayOf(type: CType<KT>, elements: List<KT>): CArray<KT> {
    return cArray(type, elements.size).apply {
        elements.forEachIndexed { index, element ->
            //TODO: Optimize
            get(index).pointed = element
        }
    }
}

//TODO: overloads for primitive arrays
public inline fun <KT : Any> ForeignCScope.cArrayOf(type: CType<KT>, vararg elements: KT): CArray<KT> {
    return cArrayOf(type, listOf(*elements))
}

public inline fun <KT : Any> ForeignCScope.cArrayOf(type: CType<KT>, builderAction: MutableList<KT>.() -> Unit): CArray<KT> {
    return cArrayOf(type, buildList(builderAction))
}

public inline fun ForeignCScope.cArrayOf(vararg elements: Byte): CArray<Byte> = cArrayCopy(elements)
//public inline fun ForeignCScope.cArrayOf(vararg elements: Int): CArray<Int> = cArrayCopy(elements)

@OptIn(ForeignMemoryApi::class)
public inline fun ForeignCScope.cArrayCopy(array: ByteArray): CArray<Byte> {
    return cArray(CType.Byte, array.size).apply {
        blockInternalC.storeByteArray(memoryAddressSizeZero(), array)
    }
}

// conversion back to Kotlin types

//TODO: same for all primitives
//TODO: recheck indexes
//TODO: which parameters?
@OptIn(ForeignMemoryApi::class)
public fun CArray<Byte>.copyInto(
    destination: ByteArray,
    destinationOffset: Int = 0,
    startIndex: Int = 0,
    endIndex: Int = destination.size
): ByteArray {
    blockInternalC.loadByteArray(memoryAddressSize(startIndex), destination, destinationOffset, endIndex)
    return destination
}

//TODO: recheck indexes
public fun CArray<Byte>.copyOf(size: Int = this.size): ByteArray = copyInto(ByteArray(size))
