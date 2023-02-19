package dev.whyoleg.ffi

import java.lang.foreign.*

public actual abstract class CVariable
internal constructor(segment: MemorySegment) : CPointed(segment)

public actual abstract class CVariableType<T : CVariable>
internal constructor() {
    internal abstract fun allocate(allocator: SegmentAllocator): T
}
