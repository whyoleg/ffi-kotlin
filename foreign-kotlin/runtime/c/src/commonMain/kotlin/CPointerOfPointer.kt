@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*

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
