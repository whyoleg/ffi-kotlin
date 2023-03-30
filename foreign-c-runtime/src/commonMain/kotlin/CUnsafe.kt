package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*

@Suppress("FunctionName")
@ForeignMemoryApi
public inline fun <R> CUnsafe(block: CUnsafe.() -> R): R = with(unsafe, block)

@ForeignMemoryApi
public sealed class CUnsafe {
    //access

    public val CPointer<*>?.address: MemoryAddress get() = (this?.segmentInternal ?: MemorySegment.Empty).address


    //constructors
    public fun <KT : Any> CPointer(type: CType<KT>, segment: MemorySegment): CPointer<KT> {
        return CPointer(type.accessor, segment)
    }

    public fun <KT : Any> CPointer(
        type: CType<KT>,
        address: MemoryAddress,
        obj: MemoryObject = MemoryObject.Default
    ): CPointer<KT>? {
        return CPointer(type, obj.unsafeMemory(address, type.layout) ?: return null)
    }

    public fun <KT : CGrouped<KT>> CGrouped(type: CType.Group<KT>, segment: MemorySegment): KT {
        return type.accessor.wrap(segment)
    }

    public fun <KT : CGrouped<KT>> CGrouped(
        type: CType.Group<KT>,
        address: MemoryAddress,
        obj: MemoryObject = MemoryObject.Default
    ): KT {
        return CGrouped(type, obj.unsafeMemory(address, type.layout)!!)
    }

    public inline fun <KT : CGrouped<KT>> CGrouped(
        type: CType.Group<KT>,
        obj: MemoryObject = MemoryObject.Default,
        block: (address: MemoryAddress) -> Unit
    ): KT {
        val segment = obj.autoAllocator.allocateMemory(type.layout)
        block(segment.address)
        return CGrouped(type, segment)
    }

}

// those things are needed, because IDEA tries to import `@PublishedApi internal object` :)
@PublishedApi
@ForeignMemoryApi
internal val unsafe: CUnsafe = Impl()

@ForeignMemoryApi
private class Impl : CUnsafe()
