package dev.whyoleg.ffi

public actual abstract class CVariable
internal constructor(memory: NativeMemory) : CPointed(memory)

public actual abstract class CVariableType<T : CVariable>(
    wrap: (NativeMemory) -> T,
    byteSize: Int,
) : CPointedType<T>(wrap, byteSize)
