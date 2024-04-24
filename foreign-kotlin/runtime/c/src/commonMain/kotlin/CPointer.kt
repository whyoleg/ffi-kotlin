@file:Suppress("NOTHING_TO_INLINE")

package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.*
import kotlin.reflect.*

public open class CPointer<KT> internal constructor(
    @PublishedApi
    internal val mapper: MemoryMapper<KT>,
    @PublishedApi
    internal val block: MemoryBlock,
    @PublishedApi
    internal val offset: MemorySizeInt
) {
    public companion object {
        public inline operator fun <KT> invoke(type: CType<KT>): CType<CPointer<KT>?> = TODO()
    }
}

// those should be extensions on CPointer and not members to be able to use declarations for primitives to avoid boxing
public inline var <KT> CPointer<KT>.value: KT
    get() = mapper.getValue(block, offset)
    set(value) = mapper.setValue(block, offset, value)

// TODO: decide on delegates
public inline operator fun <KT : Any> CPointer<KT>.getValue(thisRef: Any?, property: KProperty<*>): KT =
    TODO()

public inline operator fun <KT : Any> CPointer<KT>.setValue(thisRef: Any?, property: KProperty<*>, value: KT): Unit =
    TODO()

// TODO: needs compiler plugin
//public inline fun <reified KT> MemoryScope.allocate(): CPointer<KT> = TODO()
public inline fun <KT> MemoryScope.allocate(type: CType<KT>): CPointer<KT> = TODO()
public inline fun <KT> MemoryScope.allocateFrom(type: CType<KT>, value: KT): CPointer<KT> = TODO()

public inline fun <KT> MemoryScope.allocateFrom(value: CPointer<KT>?): CPointer<CPointer<KT>?> = TODO()

// TODO: reinterpretation

//// TODO: recheck
//@OptIn(ForeignMemoryApi::class)
//public fun <KT : Any> CPointer<*>.reinterpret(type: CType<KT>): CPointer<KT> {
//    return CPointer(type.accessor.at(accessor.offset), blockInternalC.resize(type.layout))
//}
//
//// allows specifying the size of array if a pointer needs to be interpreted as an array
//// returned array has the same scope as original pointer
//// TODO: recheck
//@OptIn(ForeignMemoryApi::class)
//public fun <KT : Any> CPointer<KT>.reinterpretAsArray(size: Int): CArray<KT> {
//    return CArray(size, accessor, blockInternalC.resize(accessor.layout, size))
//}
//
//// TODO: recheck
//@OptIn(ForeignMemoryApi::class)
//public fun <KT : Any> CPointer<*>.reinterpretAsArray(type: CType<KT>, size: Int): CArray<KT> {
//    return CArray(size, type.accessor.at(accessor.offset), blockInternalC.resize(type.layout, size))
//}

//@ForeignMemoryApi
//@JvmInline
//public value class UnsafeC internal constructor(public val arena: MemoryArena) {
//
//    public val CPointer<*>?.memoryBlock: MemoryBlock
//        get() = when (this) {
//            null -> MemoryBlock.NULL
//            else -> blockInternalC
//        }
//
//    //constructors
//    public fun <KT : Any> cPointer(type: CType<KT>, segment: MemoryBlock): CPointer<KT> {
//        return CPointer(type.accessor, segment)
//    }
//
//    public fun <KT : Any> cPointerOrNull(type: CType<KT>, segment: MemoryBlock?): CPointer<KT>? {
//        if (segment == null) return null
//        return cPointer(type, segment)
//    }
//
//    public fun <KT : Any> cPointerOrNull(type: CType<KT>, address: MemoryAddressSize): CPointer<KT>? {
//        return cPointerOrNull(type, arena.wrap(address, type.layout))
//    }
//
//    public fun <KT : CRecord<KT>> cRecord(type: CType.Record<KT>, segment: MemoryBlock): KT {
//        return type.accessor.wrap(segment)
//    }
//
//    public fun <KT : CRecord<KT>> cRecord(type: CType.Record<KT>, address: MemoryAddressSize): KT {
//        return cRecord(type, arena.wrap(address, type.layout)!!)
//    }
//
//    public inline fun <KT : CRecord<KT>> cRecord(type: CType.Record<KT>, block: (address: MemoryAddressSize) -> Unit): KT {
//        val memoryBlock = arena.allocate(type.layout)
//        block(memoryBlock.address)
//        return cRecord(type, memoryBlock)
//    }
//}
