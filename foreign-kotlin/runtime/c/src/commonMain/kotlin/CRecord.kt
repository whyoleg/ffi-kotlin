@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*

// TODO: decide on relation between record and opaque type, may be introduce `CRecord.Defined` which could be instantiated and accessed
public sealed interface CRecord {
    public fun Unsafe.memoryLayout(): MemoryLayout
}

public interface CStruct : CRecord
public interface CUnion : CRecord
public interface COpaque : CRecord

//public inline fun <KT : CRecord> MemoryScope.allocateCPointer(value: KT): CPointer<KT> = TODO()
public inline fun <KT : CRecord> MemoryScope.allocateCPointer(type: CType<KT>, block: KT.() -> Unit): CPointer<KT> =
    TODO()

// TODO: this API is really rarely needed, and it's better to use allocateCPointer instead, as there will be less copies
public inline fun <KT : CRecord> MemoryScope.allocate(type: CType<KT>, block: KT.() -> Unit = {}): KT = TODO()

@Deprecated("opaque")
public inline fun <KT : COpaque> MemoryScope.allocate(type: CType<KT>, block: KT.() -> Unit = {}): KT = TODO()

//public inline fun <KT : CRecord> CPointer<KT>.update(block: KT.() -> Unit): Unit = TODO()

// returns `pointer`
public inline fun <KT : CRecord> KT.copyTo(pointer: CPointer<KT>): CPointer<KT> = TODO()

//public inline var <KT: CRecord> CPointer<KT>.value: KT
//    get() = mapper.getValue(block, offset)
//    set(value) = mapper.setValue(block, offset, value)
//
//@Deprecated("Getting opaque value doesn't make sense")
//public inline var <KT : COpaque> CPointer<KT>.value: KT
//    get() = mapper.getValue(block, offset)
//    set(value) = mapper.setValue(block, offset, value)

// TODO: deprecate operations which doesn't make sense for COpaque types

//@Deprecated("Allocating opaque with value doesn't make sense")
//public inline fun <KT : COpaque> MemoryScope.allocateFrom(type: CType<KT>, value: KT): CPointer<KT> = TODO()
//
//@Deprecated("Allocating opaque with value doesn't make sense")
//public inline fun <KT : COpaque> MemoryScope.allocateFrom(value: KT): CPointer<KT> = TODO()

// TODO: Deprecated array from opaque
