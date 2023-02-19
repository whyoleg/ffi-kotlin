package dev.whyoleg.ffi

import java.lang.foreign.*

public actual class CULongVariable
internal constructor(segment: MemorySegment) : CVariable(segment)

public actual object CULongVariableType : CVariableType<CULongVariable>() {
    override fun allocate(allocator: SegmentAllocator): CULongVariable = CULongVariable(allocator.allocate(ValueLayout.JAVA_LONG))
}

public actual var CULongVariable.value: CULong
    get() = segment.get(ValueLayout.JAVA_LONG, 0).toULong()
    set(value) = segment.set(ValueLayout.JAVA_LONG, 0, value.toLong())
