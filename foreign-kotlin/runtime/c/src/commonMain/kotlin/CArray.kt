@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*
import kotlin.jvm.*

public typealias CArrayPointer<KT> = CPointer<KT>

@JvmInline
public value class CArray<KT : Any>
@PublishedApi internal constructor(
    @PublishedApi internal val memoryBlock: MemoryBlock,
) : Iterable<CPointer<KT>> { // TODO: Iterable vs Collection vs List type?
    public val size: Int get() = TODO()
    public operator fun get(index: Int): CPointer<KT> = TODO()
    public fun getOrNull(index: Int): CPointer<KT>? = TODO()
    override fun iterator(): Iterator<CPointer<KT>> = TODO()
}

// basic builders

public inline fun <KT : Any> MemoryScope.allocateCArray(type: CType<KT>, size: Int): CArray<KT> = TODO()
public inline fun <KT : Any> MemoryScope.allocateCArray(type: CType<KT>, size: Int, init: (Int) -> KT): CArray<KT> =
    TODO()

// unsafe
public inline fun <KT : Any> CArrayPointer<KT>.reinterpretAsArray(size: Int): CArray<KT> = TODO()

// TODO: add parameters
public inline fun <KT : Any> Array<KT>.copyInto(
    destination: CArray<KT>
): CArray<KT> = TODO()

public inline fun <KT : Any> List<KT>.copyInto(
    destination: CArray<KT>
): CArray<KT> = TODO()
