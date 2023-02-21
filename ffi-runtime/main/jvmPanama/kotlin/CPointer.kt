package dev.whyoleg.ffi

import java.lang.foreign.*

public actual class CPointer<T : CPointed>
@PublishedApi
internal constructor(
    internal val pointed: T,
)

public actual class CPointerVariable<T : CPointed>
internal constructor(
    internal val wrap: (MemorySegment) -> T,
    segment: MemorySegment,
) : CVariable(segment)

public actual var <T : CPointed> CPointerVariable<T>.value: CPointer<T>?
    get() = CPointer(segment.get(ValueLayout.ADDRESS, 0), wrap)
    set(value) = segment.set(ValueLayout.ADDRESS, 0, value.segment)

public actual val <T : CPointed> T.pointer: CPointer<T>
    get() = CPointer(this)

public actual val <T : CPointed> CPointer<T>.pointed: T
    get() = pointed


public inline fun <T : CPointed> CPointer(segment: MemorySegment, block: (MemorySegment) -> T): CPointer<T>? {
    if (segment.address() == 0L) return null
    return CPointer(block(segment))
}


//public val CPointer<*>.segment: MemorySegment get() = pointed.segment
public val CPointer<*>?.segment: MemorySegment get() = this?.pointed?.segment.orNull

public val MemorySegment?.orNull: MemorySegment get() = this ?: MemorySegment.NULL
