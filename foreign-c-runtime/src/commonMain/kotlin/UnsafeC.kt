package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import kotlin.jvm.*

@ForeignMemoryApi
@JvmInline
public value class UnsafeC internal constructor(public val arena: MemoryArena) {

    public val CPointer<*>?.address: MemoryAddress get() = (this?.segmentInternal ?: MemorySegment.Empty).address

    //constructors
    public fun <KT : Any> CPointer(type: CType<KT>, segment: MemorySegment): CPointer<KT> {
        return CPointer(type.accessor, segment)
    }

    public fun <KT : Any> CPointer(
        type: CType<KT>,
        address: MemoryAddress,
    ): CPointer<KT>? {
        return CPointer(type, arena.wrap(address, type.layout) ?: return null)
    }

    public fun <KT : CGrouped<KT>> CGrouped(type: CType.Group<KT>, segment: MemorySegment): KT {
        return type.accessor.wrap(segment)
    }

    public fun <KT : CGrouped<KT>> CGrouped(
        type: CType.Group<KT>,
        address: MemoryAddress,
    ): KT {
        return CGrouped(type, arena.wrap(address, type.layout)!!)
    }

    public inline fun <KT : CGrouped<KT>> CGrouped(
        type: CType.Group<KT>,
        block: (address: MemoryAddress) -> Unit
    ): KT {
        val segment = arena.allocate(type.layout)
        block(segment.address)
        return CGrouped(type, segment)
    }
}
