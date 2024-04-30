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
