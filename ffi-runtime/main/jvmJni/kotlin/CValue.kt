package dev.whyoleg.ffi

//TODO: should be address, not buffer?
public actual abstract class CValue<T : CVariable>(
    public val type: CVariableType<T>,
    public val memory: NativeMemory,
)

public fun <T : CVariable> cValue(
    type: CVariableType<T>,
    memory: NativeMemory,
): CValue<T> = CValueImpl(type, memory.asReadOnly())

private class CValueImpl<T : CVariable>(
    type: CVariableType<T>,
    memory: NativeMemory,
) : CValue<T>(type, memory)
