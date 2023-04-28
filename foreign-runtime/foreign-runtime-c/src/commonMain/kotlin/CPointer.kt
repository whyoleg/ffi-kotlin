package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.jvm.*
import kotlin.reflect.*

@OptIn(ForeignMemoryApi::class)
public open class CPointer<KT : Any>
internal constructor(
    @PublishedApi
    internal val accessor: MemoryAccessor<KT>,
    segment: MemoryBlock,
) : MemoryReference(segment) {
    init {
        //TODO!!!
        check(accessor.layout.size + accessor.offset <= segment.size) {
            "out of bound accessor [layout=${accessor.layout.size}, offset=${accessor.offset}, segment=${segment.size}]"
        }
    }

    @PublishedApi
    @ForeignMemoryApi
    internal val blockInternalC: MemoryBlock get() = block

    override fun toString(): String {
        return "MS[layout=${accessor.layout.size}, offset=${accessor.offset}, segment=${block.size}]"
    }
}

// those should be extensions on CPointer and not members to be able to use declarations for primitives to avoid boxing
@OptIn(ForeignMemoryApi::class)
@get:JvmName("getAnyValue")
@set:JvmName("setAnyValue")
public inline var <KT : Any> CPointer<KT>.pointed: KT?
    get() = accessor.get(blockInternalC)
    set(value) = accessor.set(blockInternalC, value)

@JvmName("getAnyValue")
public inline operator fun <KT : Any> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT? = pointed

@JvmName("setAnyValue")
public inline operator fun <KT : Any> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT?) {
    this.pointed = value
}

// factory functions

@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> ForeignCScope.cPointerOf(type: CType<KT>): CPointer<KT> = unsafe {
    cPointer(type, arena.allocate(type.layout))
}
