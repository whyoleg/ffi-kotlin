package dev.whyoleg.ffi

import java.lang.foreign.*

public actual class CUIntVariable
internal constructor(segment: MemorySegment) : CVariable(segment)

public actual object CUIntVariableType : CVariableType<CUIntVariable>(::CUIntVariable, ValueLayout.JAVA_INT)

public actual var CUIntVariable.value: CUInt
    get() = segment.get(ValueLayout.JAVA_INT, 0).toUInt()
    set(value) = segment.set(ValueLayout.JAVA_INT, 0, value.toInt())
