package dev.whyoleg.ffi

import java.lang.foreign.*

public actual abstract class CPointed
internal constructor(
    public val segment: MemorySegment,
)
