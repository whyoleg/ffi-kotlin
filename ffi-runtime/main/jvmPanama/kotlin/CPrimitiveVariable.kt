package dev.whyoleg.ffi

import java.lang.foreign.*

public actual abstract class CPrimitiveVariable
internal constructor(segment: MemorySegment) : CVariable(segment)
