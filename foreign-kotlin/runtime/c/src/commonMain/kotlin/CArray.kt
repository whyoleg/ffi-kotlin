@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*

public typealias CArrayPointer<KT> = CPointer<KT>

public class CArray<KT> internal constructor(
    public val size: Int,
    mapper: MemoryMapper<KT>,
    block: MemoryBlock,
    offset: MemorySizeInt
) : CArrayPointer<KT>(mapper, block, offset),
    // TODO: iterable vs other type?
    Iterable<CPointer<KT>> {

    public operator fun get(index: Int): CPointer<KT> = TODO()

    override fun iterator(): Iterator<CPointer<KT>> {
        TODO("Not yet implemented")
    }

    public companion object {}
}

@Deprecated("use get")
public inline var <KT> CArray<KT>.value: KT
    get() = mapper.getValue(block, offset)
    set(value) = mapper.setValue(block, offset, value)

public inline fun <KT> MemoryScope.allocateArray(type: CType<KT>, size: Int): CArray<KT> = TODO()
public inline fun <KT> MemoryScope.allocateArrayFrom(type: CType<KT>, value: Array<KT>): CArray<KT> = TODO()
public inline fun <KT> MemoryScope.allocateArrayFrom(type: CType<KT>, value: List<KT>): CArray<KT> = TODO()

// TODO
//  public inline operator fun <KT> CArray.Companion.invoke(type: CType<KT>, size: Int): CType<CArray<KT>?> = TODO()
