package dev.whyoleg.ffi

import java.lang.foreign.*

public actual abstract class CStructVariable
internal constructor(segment: MemorySegment) : CVariable(segment)
