package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*

// TODO: recheck
@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> CPointer<*>.reinterpret(type: CType<KT>): CPointer<KT> {
    return CPointer(type.accessor.at(accessor.offset), segmentInternal2.resize(type.layout))
}

// allows specifying the size of array if a pointer needs to be interpreted as an array
// returned array has the same scope as original pointer
// TODO: recheck
@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> CPointer<KT>.reinterpretAsArray(size: Int): CArray<KT> {
    return CArray(size, accessor, segmentInternal2.resize(accessor.layout, size))
}

// TODO: recheck
@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> CPointer<*>.reinterpretAsArray(type: CType<KT>, size: Int): CArray<KT> {
    return CArray(size, type.accessor.at(accessor.offset), segmentInternal2.resize(type.layout, size))
}
