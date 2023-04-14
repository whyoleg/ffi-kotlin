package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
public class OpaqueMemoryAccessor<KT : EmptyMemoryValue>(
    private val instance: KT
) : MemoryAccessor<KT>(memoryAddressSizeZero()) {
    override val layout: MemoryLayout get() = MemoryLayout.Void
    override fun get(segment: MemorySegment): KT = instance
    override fun set(segment: MemorySegment, value: KT?) {}
    override fun withOffset(offset: MemoryAddressSize): MemoryAccessor<KT> = this
}

//TODO: somehow enforce it
@ForeignMemoryApi
public inline fun <KT : EmptyMemoryValue> MemoryAccessor<KT>.getRaw(segment: MemorySegment): KT {
//    this as OpaqueMemoryAccessor
    return get(segment)!!
}

@ForeignMemoryApi
public inline fun <KT : EmptyMemoryValue> MemoryAccessor<KT>.setRaw(segment: MemorySegment, value: KT): Unit = set(segment, value)

@ForeignMemoryApi
public inline operator fun <KT : EmptyMemoryValue> MemoryAccessor<KT>.getValue(
    thisRef: MemoryHolder,
    property: KProperty<*>
): KT = getRaw(thisRef.segmentInternal)

@ForeignMemoryApi
public inline operator fun <KT : EmptyMemoryValue> MemoryAccessor<KT>.setValue(
    thisRef: MemoryHolder,
    property: KProperty<*>,
    value: KT
): Unit = setRaw(thisRef.segmentInternal, value)
