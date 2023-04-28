package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
public class OpaqueMemoryAccessor<KT : EmptyMemoryValue>(
    private val instance: KT
) : MemoryAccessor<KT>(memoryAddressSizeZero()) {
    override val layout: MemoryBlockLayout get() = MemoryBlockLayout.Void
    override fun get(block: MemoryBlock): KT = instance
    override fun set(block: MemoryBlock, value: KT?) {}
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<KT> = this
}

//TODO: somehow enforce it
@ForeignMemoryApi
public inline fun <KT : EmptyMemoryValue> MemoryAccessor<KT>.getRaw(block: MemoryBlock): KT {
//    this as OpaqueMemoryAccessor
    return get(block)!!
}

@ForeignMemoryApi
public inline fun <KT : EmptyMemoryValue> MemoryAccessor<KT>.setRaw(block: MemoryBlock, value: KT): Unit = set(block, value)

@ForeignMemoryApi
public inline operator fun <KT : EmptyMemoryValue> MemoryAccessor<KT>.getValue(
    thisRef: MemoryHolder,
    property: KProperty<*>
): KT = getRaw(thisRef.blockInternal)

@ForeignMemoryApi
public inline operator fun <KT : EmptyMemoryValue> MemoryAccessor<KT>.setValue(
    thisRef: MemoryHolder,
    property: KProperty<*>,
    value: KT
): Unit = setRaw(thisRef.blockInternal, value)
