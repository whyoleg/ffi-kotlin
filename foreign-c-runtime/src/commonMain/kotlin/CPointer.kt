@file:OptIn(ForeignMemoryApi::class)

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.jvm.*
import kotlin.reflect.*

public open class CPointer<KT : Any>
internal constructor(
    @PublishedApi
    internal val accessor: MemoryAccessor<KT>,
    segment: MemorySegment,
) : MemoryReference(segment) {
    init {
        check(accessor.layout.size + accessor.offset < segment.size) { "out of bound accessor" }
    }

    @PublishedApi
    @ForeignMemoryApi
    internal val segmentInternal: MemorySegment get() = segment
}

// those should be extensions on CPointer and not members to be able to use declarations for primitives to avoid boxing
@get:JvmName("getAnyValue")
@set:JvmName("setAnyValue")
public inline var <KT : Any> CPointer<KT>.pointed: KT?
    get() = accessor.get(segmentInternal)
    set(value) = accessor.set(segmentInternal, value)

@JvmName("getAnyValue")
public inline operator fun <KT : Any> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT? = pointed

@JvmName("setAnyValue")
public inline operator fun <KT : Any> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT?) {
    this.pointed = value
}

public fun <KT : Any> MemoryScope.allocatePointerFor(type: CType<KT>): CPointer<KT> {
    return unsafe.CPointer(type, allocateMemory(type.layout))
}

public fun <KT : Any> MemoryScope.allocatePointerFor(type: CType<KT>, value: KT?): CPointer<KT> {
    return allocatePointerFor(type).apply { pointed = value }
}

// TODO: recheck
public fun <KT : Any> CPointer<*>.reinterpret(type: CType<KT>): CPointer<KT> {
    return type.pointer.accessor.get(segmentInternal.loadSegment(accessor.offset, type.layout))!!
}
