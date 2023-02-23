package dev.whyoleg.ffi

import java.lang.foreign.*

public actual abstract class CVariable
internal constructor(segment: MemorySegment) : CPointed(segment)

public actual abstract class CVariableType<T : CVariable>(
    wrap: (MemorySegment) -> T,
    public val layout: MemoryLayout,
) : CPointedType<T>(wrap)
