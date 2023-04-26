package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import kotlin.jvm.*

@ForeignMemoryApi
@JvmInline
public value class UnsafeC internal constructor(public val arena: MemoryArena) {

    public val CPointer<*>?.address: MemoryAddress
        get() = when (this) {
            null -> nullMemoryAddress()
            else -> segmentInternal2.address
        }

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

    public fun <KT : CRecord<KT>> CRecord(type: CType.Record<KT>, segment: MemorySegment): KT {
        return type.accessor.wrap(segment)
    }

    public fun <KT : CRecord<KT>> CRecord(
        type: CType.Record<KT>,
        address: MemoryAddress,
    ): KT {
        return CRecord(type, arena.wrap(address, type.layout)!!)
    }

    public inline fun <KT : CRecord<KT>> CRecord(
        type: CType.Record<KT>,
        block: (address: MemoryAddress) -> Unit
    ): KT {
        val segment = arena.allocate(type.layout)
        block(segment.address)
        return CRecord(type, segment)
    }
}
