package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.jvm.*
import kotlin.reflect.*

@OptIn(ForeignMemoryApi::class)
public abstract class CStruct<Self : CStruct<Self>>
@ForeignMemoryApi
constructor(segment: MemorySegment) : CGrouped<Self>(segment) {
    public abstract override val type: CType.Struct<Self>
}

@OptIn(ForeignMemoryApi::class)
public abstract class CUnion<Self : CUnion<Self>>
@ForeignMemoryApi
constructor(segment: MemorySegment) : CGrouped<Self>(segment) {
    public abstract override val type: CType.Union<Self>
}

//TODO: rename to CRecord
@OptIn(ForeignMemoryApi::class)
public sealed class CGrouped<Self : CGrouped<Self>>(segment: MemorySegment) : MemoryValue(segment) {
    public abstract val type: CType.Group<Self>
}

@OptIn(ForeignMemoryApi::class)
@get:JvmName("getGroupValue")
@set:JvmName("setGroupValue")
public inline var <KT : CGrouped<KT>> CPointer<KT>.pointed: KT
    get() = accessor.getRaw(segmentInternal2)
    set(value) = accessor.setRaw(segmentInternal2, value)

@JvmName("getGroupValue")
public inline operator fun <KT : CGrouped<KT>> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT = pointed

@JvmName("setGroupValue")
public inline operator fun <KT : CGrouped<KT>> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT) {
    this.pointed = value
}

// factory functions

public inline fun <KT : CGrouped<KT>> ForeignCScope.cPointerOf(value: KT): CPointer<KT> =
    cPointerOf(value.type).apply { pointed = value }

public inline fun <KT : CGrouped<KT>> ForeignCScope.cPointerOf(type: CType.Group<KT>, block: KT.() -> Unit): CPointer<KT> =
    cPointerOf(type).apply { pointed.block() }

@OptIn(ForeignMemoryApi::class)
public inline fun <KT : CGrouped<KT>> ForeignCScope.cGroupedOf(type: CType.Group<KT>, block: KT.() -> Unit = {}): KT = unsafe {
    CGrouped(type, arena.allocate(type.layout)).apply(block)
}

public inline fun <KT : CStruct<KT>> ForeignCScope.cStructOf(type: CType.Struct<KT>, block: KT.() -> Unit = {}): KT =
    cGroupedOf(type, block)

public inline fun <KT : CUnion<KT>> ForeignCScope.cUnionOf(type: CType.Union<KT>, block: KT.() -> Unit = {}): KT = cGroupedOf(type, block)
