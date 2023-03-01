package dev.whyoleg.ffi.c

import java.lang.foreign.*

public actual class CPointer<T : CPointed>
@PublishedApi
internal constructor(
    internal val pointed: T, //TODO: may be use just segment here?
)

public actual class CPointerVariable<T : CPointed>
internal constructor(
    segment: MemorySegment,
    internal val type: CPointedType<T>,
) : CVariable(segment)

public actual var <T : CPointed> CPointerVariable<T>.value: CPointer<T>?
    get() = CPointer(segment.get(ValueLayout.ADDRESS, 0), type)
    set(value) = segment.set(ValueLayout.ADDRESS, 0, value.segment)

public actual val <T : CPointed> T.pointer: CPointer<T>
    get() = CPointer(this)

public actual val <T : CPointed> CPointer<T>.pointed: T
    get() = pointed

public fun <T : CPointed> CPointer(segment: MemorySegment, type: CPointedType<T>): CPointer<T>? {
    if (segment.address() == 0L) return null
    return CPointer(type.wrap(segment))
}

public fun CPointer(segment: MemorySegment): CPointer<out CPointed>? {
    if (segment.address() == 0L) return null
    return CPointer(COpaqueImpl(segment))
}

//public val CPointer<*>.segment: MemorySegment get() = pointed.segment
public val CPointer<*>?.segment: MemorySegment get() = this?.pointed?.segment.orNull

public val MemorySegment?.orNull: MemorySegment get() = this ?: MemorySegment.NULL
