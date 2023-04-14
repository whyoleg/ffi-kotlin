package dev.whyoleg.foreign.c

import dev.whyoleg.foreign.memory.*

// allows specifying the size of array if a pointer needs to be interpreted as an array
// returned array has the same scope as original pointer
// TODO: naming
@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> CPointer<KT>.asCArray(size: Int): CArray<KT> {
    return CArray(size, accessor, segmentInternal2.resize(accessor.layout, size))
}

// truly unsafe function
// should be used only(?) for converting C string (returned from function or struct) to Kotlin string, as we don't know the size of it
// returned pointer has the same scope as an original pointer
// TODO: naming
@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> CPointer<KT>.asUnbounded(): CPointer<KT> {
    return CPointer(accessor, segmentInternal2.asUnbounded())
}

// TODO: recheck
@OptIn(ForeignMemoryApi::class)
public fun <KT : Any> CPointer<*>.reinterpret(type: CType<KT>): CPointer<KT> {
    return CPointer(type.accessor.at(accessor.offset), segmentInternal2.resize(type.layout))
}
