package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*

@Suppress("FunctionName")
@ForeignMemoryApi
public inline fun <R> CUnsafe(block: CUnsafe.() -> R): R = with(unsafe, block)

@ForeignMemoryApi
public sealed class CUnsafe {
    //acccess

    public val CPointer<*>?.address: MemoryAddress get() = (this?.segmentInternal ?: MemorySegment.Empty).address


    //constructors
    public fun <KT : Any> CPointer(type: CType<KT>, segment: MemorySegment): CPointer<KT> {
        //TODO: null check?
        return CPointer(type.accessor, segment)
    }

    public fun <KT : Any> CPointer(type: CType<KT>, address: MemoryAddress): CPointer<KT>? {
        return CPointer(type, MemorySegment.fromAddress(address, type.layout) ?: return null)
    }

    public fun <KT : CGrouped<KT>> CGrouped(type: CType.Group<KT>, segment: MemorySegment): KT {
        return type.accessor.wrap(segment)
    }

    public fun <KT : CGrouped<KT>> CGrouped(type: CType.Group<KT>, address: MemoryAddress): KT {
        return type.accessor.wrap(MemorySegment.fromAddress(address, type.layout)!!)
    }

    public inline fun <KT : CGrouped<KT>> CGrouped(
        type: CType.Group<KT>,
        allocator: MemoryAllocator,
        block: (address: MemoryAddress) -> Unit
    ): KT {
        val segment = allocator.allocateMemory(type.layout)
        block(segment.address)
        return type.accessor.wrap(segment)
    }

}

// those things are needed, because IDEA tries to import `@PublishedApi internal object` :)
@PublishedApi
@ForeignMemoryApi
internal val unsafe: CUnsafe = Impl()

@ForeignMemoryApi
private class Impl : CUnsafe()
