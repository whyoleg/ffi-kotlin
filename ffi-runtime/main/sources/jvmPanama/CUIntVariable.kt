package dev.whyoleg.ffi

import java.lang.foreign.*

public actual class CUIntVariable
internal constructor(segment: MemorySegment) : CVariable(segment)

public actual object CUIntVariableType : CVariableType<CUIntVariable>() {
    override fun allocate(allocator: SegmentAllocator): CUIntVariable = CUIntVariable(allocator.allocate(ValueLayout.JAVA_INT))
}

public actual var CUIntVariable.value: CUInt
    get() = segment.get(ValueLayout.JAVA_INT, 0).toUInt()
    set(value) = segment.set(ValueLayout.JAVA_INT, 0, value.toInt())
