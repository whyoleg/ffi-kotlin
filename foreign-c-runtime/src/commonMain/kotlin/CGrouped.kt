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

@OptIn(ForeignMemoryApi::class)
public sealed class CGrouped<Self : CGrouped<Self>>
@ForeignMemoryApi
constructor(segment: MemorySegment) : MemoryValue(segment) {
    public abstract val type: CType.Group<Self>
}

@get:JvmName("getGroupValue")
@set:JvmName("setGroupValue")
@OptIn(ForeignMemoryApi::class)
public inline var <KT : CGrouped<KT>> CPointer<KT>.pointed: KT
    get() = accessor.getRaw(segment)
    set(value) = accessor.setRaw(segment, value)

@JvmName("getGroupValue")
public inline operator fun <KT : CGrouped<KT>> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT = pointed

@JvmName("setGroupValue")
public inline operator fun <KT : CGrouped<KT>> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT) {
    this.pointed = value
}

public inline fun <KT : CGrouped<KT>> MemoryScope.pointer(value: KT): CPointer<KT> = pointerFor(value.type, value)

public inline fun <KT : CGrouped<KT>> MemoryScope.pointer(type: CType.Group<KT>, block: KT.() -> Unit): CPointer<KT> =
    pointerFor(type).apply { pointed.block() }

@OptIn(ForeignMemoryApi::class)
public inline fun <KT : CGrouped<KT>> MemoryScope.struct(type: CType.Group<KT>, block: KT.() -> Unit = {}): KT {
    return type.accessor.get(allocateMemory(type.layout)).apply(block)
}
