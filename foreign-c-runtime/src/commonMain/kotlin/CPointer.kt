@file:OptIn(ForeignMemoryApi::class)

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.jvm.*
import kotlin.reflect.*

public typealias UnsafeCPointer<KT> = CPointer<KT>

public open class CPointer<KT : Any>
internal constructor(
    @PublishedApi
    internal val accessor: MemoryAccessor<KT>,
    segment: MemorySegment,
) : MemoryReference(segment) {
    init {
        //TODO!!!
        check(accessor.layout.size + accessor.offset <= segment.size) {
            "out of bound accessor [layout=${accessor.layout.size}, offset=${accessor.offset}, segment=${segment.size}]"
        }
    }

    @PublishedApi
    @ForeignMemoryApi
    internal val segmentInternal2: MemorySegment get() = segment

    override fun toString(): String {
        return "MS[layout=${accessor.layout.size}, offset=${accessor.offset}, segment=${segment.size}]"
    }
}

// those should be extensions on CPointer and not members to be able to use declarations for primitives to avoid boxing
@get:JvmName("getAnyValue")
@set:JvmName("setAnyValue")
public inline var <KT : Any> CPointer<KT>.pointed: KT?
    get() = accessor.get(segmentInternal2)
    set(value) = accessor.set(segmentInternal2, value)

@JvmName("getAnyValue")
public inline operator fun <KT : Any> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT? = pointed

@JvmName("setAnyValue")
public inline operator fun <KT : Any> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT?) {
    this.pointed = value
}

public fun <KT : Any> ForeignCScope.cPointerOf(type: CType<KT>): CPointer<KT> = unsafe {
    CPointer(type, arena.allocate(type.layout))
}

// TODO: recheck
public fun <KT : Any> CPointer<*>.reinterpret(type: CType<KT>): CPointer<KT> {
    return type.pointer.accessor.get(segmentInternal2.loadSegment(accessor.offset, type.layout))!!
}
