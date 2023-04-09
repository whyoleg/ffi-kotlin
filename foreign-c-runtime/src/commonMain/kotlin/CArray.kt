@file:OptIn(ForeignMemoryApi::class)

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*

public typealias CArrayPointer<KT> = CPointer<KT>

public class CArray<KT : Any>
internal constructor(
    public val size: Int,
    accessor: MemoryAccessor<KT>,
    segment: MemorySegment,
) : CArrayPointer<KT>(accessor, segment)

public operator fun <KT : Any> CArray<KT>.get(index: Int): CPointer<KT> {
    if (index == 0) return this
    check(index < size) { "Array out of bound access" }

    //TODO: check index ON access?
    return CPointer(
        accessor.at(accessor.offset + accessor.layout.size * index),
        segmentInternal
    )
}

public fun <KT : Any> ForeignCScope.allocateArrayFor(type: CType<KT>, size: Int): CArray<KT> = unsafe {
    CArray(size, type.accessor, arena.allocateArray(type.layout, size))
}

public fun <KT : Any> ForeignCScope.cArrayOf(type: CType<KT>, elements: List<KT>): CArray<KT> {
    return allocateArrayFor(type, elements.size).apply {
        elements.forEachIndexed { index, element ->
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

public inline fun ForeignCScope.cArray(array: ByteArray): CArray<Byte> {
    return allocateArrayFor(CType.Byte, array.size).apply {
        segmentInternal.storeByteArray(memoryAddressSizeZero(), array)
    }
}

//TODO: same for all primitives
//TODO: naming
public fun CArray<Byte>.copyInto(destination: ByteArray, destinationOffset: Int = 0, startIndex: Int = 0, endIndex: Int = size): ByteArray {
    segmentInternal.loadByteArray(memoryAddressSize(startIndex), destination, destinationOffset, TODO())
    return destination
}

public fun CArray<Byte>.copyOf(size: Int = this.size): ByteArray = copyInto(ByteArray(size))
