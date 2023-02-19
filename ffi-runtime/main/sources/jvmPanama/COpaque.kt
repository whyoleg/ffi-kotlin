package dev.whyoleg.ffi

import java.lang.foreign.*

public actual abstract class COpaque(segment: MemorySegment) : CPointed(segment)

public fun COpaquePointer(segment: MemorySegment): CPointer<COpaque>? = CPointer(segment, ::COpaqueImpl)

private class COpaqueImpl(segment: MemorySegment) : COpaque(segment)
