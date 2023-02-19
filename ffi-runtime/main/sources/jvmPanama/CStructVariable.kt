package dev.whyoleg.ffi

import java.lang.foreign.*

public actual abstract class CStructVariable(segment: MemorySegment) : CVariable(segment)
