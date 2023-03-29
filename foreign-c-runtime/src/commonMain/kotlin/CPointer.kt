package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.jvm.*
import kotlin.reflect.*

// TODO: recheck constructor
@OptIn(ForeignMemoryApi::class)
public class CPointer<KT : Any>
internal constructor(
    public val accessor: MemoryAccessor<KT>,
    segment: MemorySegment,
) : MemoryReference(segment) {
    public companion object
}

// those should be extensions on CPointer and not members to be able to use declarations for primitives to avoid boxing
@get:JvmName("getAnyValue")
@set:JvmName("setAnyValue")
@OptIn(ForeignMemoryApi::class)
public inline var <KT : Any> CPointer<KT>.pointed: KT?
    get() = accessor.get(segment)
    set(value) = accessor.set(segment, value)

@JvmName("getAnyValue")
public inline operator fun <KT : Any> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT? = pointed

@JvmName("setAnyValue")
public inline operator fun <KT : Any> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT?) {
    this.pointed = value
}

@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> MemoryScope.pointerFor(type: CType<KT>, value: KT? = null): CPointer<KT> {
    return type.pointer.accessor.get(allocateMemory(type.layout))!!.apply { this.pointed = value }
}

// TODO: recheck
@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> CPointer<*>.reinterpret(type: CType<KT>): CPointer<KT> {
    return type.pointer.accessor.get(segment.loadSegment(accessor.offset, type.layout))!!
}
