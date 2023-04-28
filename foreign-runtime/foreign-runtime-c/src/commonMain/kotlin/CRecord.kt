package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.memory.access.*
import kotlin.jvm.*
import kotlin.reflect.*

@OptIn(ForeignMemoryApi::class)
public abstract class CStruct<Self : CStruct<Self>>
@ForeignMemoryApi
constructor(segment: MemoryBlock) : CRecord<Self>(segment) {
    public abstract override val type: CType.Struct<Self>
}

@OptIn(ForeignMemoryApi::class)
public abstract class CUnion<Self : CUnion<Self>>
@ForeignMemoryApi
constructor(segment: MemoryBlock) : CRecord<Self>(segment) {
    public abstract override val type: CType.Union<Self>
}

@OptIn(ForeignMemoryApi::class)
public sealed class CRecord<Self : CRecord<Self>>(segment: MemoryBlock) : MemoryValue(segment) {
    public abstract val type: CType.Record<Self>
}

@OptIn(ForeignMemoryApi::class)
@get:JvmName("getRecordValue")
@set:JvmName("setRecordValue")
public inline var <KT : CRecord<KT>> CPointer<KT>.pointed: KT
    get() = accessor.getRaw(blockInternalC)
    set(value) = accessor.setRaw(blockInternalC, value)

@JvmName("getRecordValue")
public inline operator fun <KT : CRecord<KT>> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT = pointed

@JvmName("setRecordValue")
public inline operator fun <KT : CRecord<KT>> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT) {
    this.pointed = value
}

// factory functions

public inline fun <KT : CRecord<KT>> ForeignCScope.cPointerOf(value: KT): CPointer<KT> =
    cPointerOf(value.type).apply { pointed = value }

public inline fun <KT : CRecord<KT>> ForeignCScope.cPointerOf(type: CType.Record<KT>, block: KT.() -> Unit): CPointer<KT> =
    cPointerOf(type).apply { pointed.block() }

@OptIn(ForeignMemoryApi::class)
public inline fun <KT : CRecord<KT>> ForeignCScope.cRecordOf(type: CType.Record<KT>, block: KT.() -> Unit = {}): KT = unsafe {
    cRecord(type, arena.allocate(type.layout)).apply(block)
}

public inline fun <KT : CStruct<KT>> ForeignCScope.cStructOf(type: CType.Struct<KT>, block: KT.() -> Unit = {}): KT =
    cRecordOf(type, block)

public inline fun <KT : CUnion<KT>> ForeignCScope.cUnionOf(type: CType.Union<KT>, block: KT.() -> Unit = {}): KT = cRecordOf(type, block)
