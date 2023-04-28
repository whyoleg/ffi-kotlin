package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import kotlin.jvm.*

@ForeignMemoryApi
@JvmInline
public value class UnsafeC internal constructor(public val arena: MemoryArena) {

    public val CPointer<*>?.memoryBlock: MemoryBlock
        get() = when (this) {
            null -> MemoryBlock.NULL
            else -> blockInternalC
        }

    //constructors
    public fun <KT : Any> cPointer(type: CType<KT>, segment: MemoryBlock): CPointer<KT> {
        return CPointer(type.accessor, segment)
    }

    public fun <KT : Any> cPointerOrNull(type: CType<KT>, segment: MemoryBlock?): CPointer<KT>? {
        if (segment == null) return null
        return cPointer(type, segment)
    }

    public fun <KT : Any> cPointerOrNull(type: CType<KT>, address: MemoryAddressSize): CPointer<KT>? {
        return cPointerOrNull(type, arena.wrap(address, type.layout))
    }

    public fun <KT : CRecord<KT>> cRecord(type: CType.Record<KT>, segment: MemoryBlock): KT {
        return type.accessor.wrap(segment)
    }

    public fun <KT : CRecord<KT>> cRecord(type: CType.Record<KT>, address: MemoryAddressSize): KT {
        return cRecord(type, arena.wrap(address, type.layout)!!)
    }

    public inline fun <KT : CRecord<KT>> cRecord(type: CType.Record<KT>, block: (address: MemoryAddressSize) -> Unit): KT {
        val memoryBlock = arena.allocate(type.layout)
        block(memoryBlock.address)
        return cRecord(type, memoryBlock)
    }
}
