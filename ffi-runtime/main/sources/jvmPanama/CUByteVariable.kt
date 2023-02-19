package dev.whyoleg.ffi

import java.lang.foreign.*

public actual class CUByteVariable
internal constructor(segment: MemorySegment) : CVariable(segment)

public actual object CUByteVariableType : CVariableType<CUByteVariable>() {
    override fun allocate(allocator: SegmentAllocator): CUByteVariable = CUByteVariable(allocator.allocate(ValueLayout.JAVA_BYTE))
}

public actual var CUByteVariable.value: CUByte
    get() = segment.get(ValueLayout.JAVA_BYTE, 0).toUByte()
    set(value) = segment.set(ValueLayout.JAVA_BYTE, 0, value.toByte())
