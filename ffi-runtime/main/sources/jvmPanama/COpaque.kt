package dev.whyoleg.ffi

import java.lang.foreign.*

public actual abstract class COpaque(segment: MemorySegment) : CPointed(segment)
