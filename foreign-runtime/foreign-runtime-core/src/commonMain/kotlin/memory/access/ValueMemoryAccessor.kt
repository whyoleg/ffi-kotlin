package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
public abstract class ValueMemoryAccessor<KT : MemoryValue>(offset: MemoryAddressSize) : MemoryAccessor<KT>(offset) {
    public abstract fun wrap(block: MemoryBlock): KT

    final override fun get(block: MemoryBlock): KT =
        wrap(block.loadSegment(offset, layout))

    final override fun set(block: MemoryBlock, value: KT?): Unit =
        block.storeSegment(offset, layout, value?.blockInternal ?: MemoryBlock.NULL)
}

//TODO: somehow enforce it
@ForeignMemoryApi
public inline fun <KT : MemoryValue> MemoryAccessor<KT>.getRaw(block: MemoryBlock): KT {
//    this as ValueMemoryAccessor
    return get(block)!!
}

@ForeignMemoryApi
public inline fun <KT : MemoryValue> MemoryAccessor<KT>.setRaw(block: MemoryBlock, value: KT): Unit = set(block, value)

@ForeignMemoryApi
public inline operator fun <KT : MemoryValue> MemoryAccessor<KT>.getValue(
    thisRef: MemoryHolder,
    property: KProperty<*>
): KT = getRaw(thisRef.blockInternal)

@ForeignMemoryApi
public inline operator fun <KT : MemoryValue> MemoryAccessor<KT>.setValue(
    thisRef: MemoryHolder,
    property: KProperty<*>,
    value: KT
): Unit = setRaw(thisRef.blockInternal, value)
