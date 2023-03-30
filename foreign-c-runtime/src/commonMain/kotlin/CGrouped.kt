@file:OptIn(ForeignMemoryApi::class)

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.jvm.*
import kotlin.reflect.*

public abstract class CStruct<Self : CStruct<Self>>
@ForeignMemoryApi
constructor(segment: MemorySegment) : CGrouped<Self>(segment) {
    public abstract override val type: CType.Struct<Self>
}

public abstract class CUnion<Self : CUnion<Self>>
@ForeignMemoryApi
constructor(segment: MemorySegment) : CGrouped<Self>(segment) {
    public abstract override val type: CType.Union<Self>
}

public sealed class CGrouped<Self : CGrouped<Self>>(segment: MemorySegment) : MemoryValue(segment) {
    public abstract val type: CType.Group<Self>
}

@get:JvmName("getGroupValue")
@set:JvmName("setGroupValue")
public inline var <KT : CGrouped<KT>> CPointer<KT>.pointed: KT
    get() = accessor.getRaw(segmentInternal)
    set(value) = accessor.setRaw(segmentInternal, value)

@JvmName("getGroupValue")
public inline operator fun <KT : CGrouped<KT>> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT = pointed

@JvmName("setGroupValue")
public inline operator fun <KT : CGrouped<KT>> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT) {
    this.pointed = value
}

public inline fun <KT : CGrouped<KT>> MemoryScope.pointer(value: KT): CPointer<KT> = pointerFor(value.type, value)

public inline fun <KT : CGrouped<KT>> MemoryScope.pointer(type: CType.Group<KT>, block: KT.() -> Unit): CPointer<KT> =
    pointerFor(type).apply { pointed.block() }

public inline fun <KT : CGrouped<KT>> MemoryScope.struct(type: CType.Group<KT>, block: KT.() -> Unit = {}): KT {
    return unsafe.CGrouped(type, allocateMemory(type.layout)).apply(block)
}
