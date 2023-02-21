package dev.whyoleg.ffi

import java.lang.foreign.*

public actual abstract class CValue<T : CVariable>(
    public val type: CVariableType<T>,
    public val segment: MemorySegment,
)

public fun <T : CVariable> cValue(
    type: CVariableType<T>,
    segment: MemorySegment,
): CValue<T> = CValueImpl(type, segment.asReadOnly())

private class CValueImpl<T : CVariable>(
    type: CVariableType<T>,
    segment: MemorySegment,
) : CValue<T>(type, segment)
