package dev.whyoleg.ffi.c

import java.lang.foreign.*

public actual abstract class CPointed
internal constructor(
    public val segment: MemorySegment,
)

public actual abstract class CPointedType<T : CPointed>
internal constructor(
    internal val wrap: (MemorySegment) -> T,
)

public actual abstract class COpaque(segment: MemorySegment) : CPointed(segment)
public actual abstract class COpaqueType<T : COpaque>(wrap: (MemorySegment) -> T) : CPointedType<T>(wrap)

internal class COpaqueImpl(segment: MemorySegment) : COpaque(segment)

public actual abstract class CVariable internal constructor(segment: MemorySegment) : CPointed(segment)

public actual abstract class CVariableType<T : CVariable>(wrap: (MemorySegment) -> T, public val layout: MemoryLayout) :
    CPointedType<T>(wrap)

public actual abstract class CStructVariable(segment: MemorySegment) : CVariable(segment)
