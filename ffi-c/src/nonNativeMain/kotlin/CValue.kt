package dev.whyoleg.ffi.c

//TODO? redesign...
public actual abstract class CValue<T : CVariable>
internal constructor(
    public val memory: NativeMemory,
    public val type: CVariableType<T>,
)

internal class CValueImpl<T : CVariable>(memory: NativeMemory, type: CVariableType<T>) : CValue<T>(memory, type)
