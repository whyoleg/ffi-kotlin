package dev.whyoleg.ffi

import java.lang.foreign.*

public actual abstract class CPointed
internal constructor(
    public val segment: MemorySegment,
)

public actual abstract class CPointedType<T : CPointed>
internal constructor(
    internal val wrap: (MemorySegment) -> T,
)
