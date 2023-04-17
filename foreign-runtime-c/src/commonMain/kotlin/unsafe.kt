package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*

// allows specifying the size of array if a pointer needs to be interpreted as an array
// returned array has the same scope as original pointer
// TODO: naming
@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> CPointer<KT>.asCArray(size: Int): CArray<KT> {
    return CArray(size, accessor, segmentInternal2.resize(accessor.layout, size))
}

// TODO: recheck
@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> CPointer<*>.reinterpret(type: CType<KT>): CPointer<KT> {
    return CPointer(type.accessor.at(accessor.offset), segmentInternal2.resize(type.layout))
}
