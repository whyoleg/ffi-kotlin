package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.ForeignMemoryApi
import dev.whyoleg.foreign.memory.MemoryHolder
import dev.whyoleg.foreign.memory.MemoryScope
import dev.whyoleg.foreign.memory.MemorySegment
import dev.whyoleg.foreign.memory.access.MemoryAccessor
import kotlin.reflect.KProperty

// TODO: recheck constructor
@OptIn(ForeignMemoryApi::class)
public class CPointer<KT : Any>
internal constructor(
    public val accessor: MemoryAccessor<KT>,
    segment: MemorySegment,
) : MemoryHolder(segment)

// those should be extensions on CPointer and not members to be able to use declarations for primitives to avoid boxing
@OptIn(ForeignMemoryApi::class)
public inline var <KT : Any> CPointer<KT>.value: KT?
    get() = accessor.get(segment)
    set(value) = accessor.set(segment, value)

public inline operator fun <KT : Any> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT? = value
public inline operator fun <KT : Any> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT?) {
    this.value = value
}

@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> MemoryScope.pointerFor(type: CType<KT>, value: KT? = null): CPointer<KT> {
    return type.pointer.accessor.get(allocateMemory(type.layout))!!.apply { this.value = value }
}

// TODO: recheck
@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> CPointer<*>.reinterpret(type: CType<KT>): CPointer<KT> {
    return type.pointer.accessor.get(segment.loadSegment(accessor.offset, type.layout))!!
}
