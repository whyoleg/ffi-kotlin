package dev.whyoleg.ffi

import java.lang.foreign.*

public actual class CLongVariable
internal constructor(segment: MemorySegment) : CVariable(segment)

public actual object CLongVariableType : CVariableType<CLongVariable>(::CLongVariable, ValueLayout.JAVA_LONG)

public actual var CLongVariable.value: CLong
    get() = segment.get(ValueLayout.JAVA_LONG, 0)
    set(value) = segment.set(ValueLayout.JAVA_LONG, 0, value)
