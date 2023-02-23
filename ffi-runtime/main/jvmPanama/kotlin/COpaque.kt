package dev.whyoleg.ffi

import java.lang.foreign.*

public actual abstract class COpaque(segment: MemorySegment) : CPointed(segment)
public actual abstract class COpaqueType<T : COpaque>(wrap: (MemorySegment) -> T) : CPointedType<T>(wrap)

internal class COpaqueImpl(segment: MemorySegment) : COpaque(segment)
