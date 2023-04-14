package dev.whyoleg.foreign.memory.access

import dev.whyoleg.foreign.memory.*
import kotlin.reflect.*

@ForeignMemoryApi
public abstract class ValueMemoryAccessor<KT : MemoryValue>(offset: MemoryAddressSize) : MemoryAccessor<KT>(offset) {
    public abstract fun wrap(segment: MemorySegment): KT

    final override fun get(segment: MemorySegment): KT =
        wrap(segment.loadSegment(offset, layout))

    final override fun set(segment: MemorySegment, value: KT?): Unit =
        segment.storeSegment(offset, layout, value?.segmentInternal ?: MemorySegment.Empty)
}

//TODO: somehow enforce it
@ForeignMemoryApi
public inline fun <KT : MemoryValue> MemoryAccessor<KT>.getRaw(segment: MemorySegment): KT {
//    this as ValueMemoryAccessor
    return get(segment)!!
}

@ForeignMemoryApi
public inline fun <KT : MemoryValue> MemoryAccessor<KT>.setRaw(segment: MemorySegment, value: KT): Unit = set(segment, value)

@ForeignMemoryApi
public inline operator fun <KT : MemoryValue> MemoryAccessor<KT>.getValue(
    thisRef: MemoryHolder,
    property: KProperty<*>
): KT = getRaw(thisRef.segmentInternal)

@ForeignMemoryApi
public inline operator fun <KT : MemoryValue> MemoryAccessor<KT>.setValue(
    thisRef: MemoryHolder,
    property: KProperty<*>,
    value: KT
): Unit = setRaw(thisRef.segmentInternal, value)
