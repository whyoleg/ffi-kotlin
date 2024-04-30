@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*
import kotlin.jvm.*

@JvmInline
public value class CPointer<KT : Any>
@PublishedApi internal constructor(
    @PublishedApi internal val memoryBlock: MemoryBlock,
) {
    public companion object {
        // TODO: should be replaced with compiler plugin
        // for now, just a shortcut for nicer API
        public inline operator fun <KT : Any> invoke(type: CType<KT>): CType<CPointer<KT>> = CType.CPointer(type)
    }
}

// basic builders

public inline fun <KT : Any> MemoryScope.allocateCPointer(type: CType<KT>): CPointer<KT> = TODO()

// unsafe

public inline fun <KT : Any> CPointer<*>.reinterpret(type: CType<KT>): CPointer<KT> = TODO()

// pointer of pointer (mostly the same for other primitives)

public inline var <KT : CPointer<*>> CPointer<KT>.pointed: KT?
    get() = TODO()
    set(value) = TODO()

public inline fun <KT : CPointer<*>> MemoryScope.allocateCPointer(type: CType<KT>, pointed: KT): CPointer<KT> = TODO()
public inline fun <KT : CPointer<*>> MemoryScope.allocateCArray(type: CType<KT>, elements: List<KT>): CArray<KT> =
    TODO()

public inline fun <KT : CPointer<*>> MemoryScope.allocateCArray(type: CType<KT>, elements: Array<KT>): CArray<KT> =
    TODO()

public inline fun <KT : CPointer<*>> MemoryScope.allocateCArrayOf(type: CType<KT>, vararg elements: KT): CArray<KT> =
    TODO()
