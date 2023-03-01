package dev.whyoleg.ffi.c

public actual abstract class CPointed internal constructor(public val memory: NativeMemory) {
    public abstract val type: CPointedType<*>
}

public actual abstract class CPointedType<T : CPointed>(
    internal val wrap: (NativeMemory) -> T,
    internal val byteSize: Int,
)

public actual abstract class COpaque(memory: NativeMemory) : CPointed(memory)
public actual abstract class COpaqueType<T : COpaque>(wrap: (NativeMemory) -> T) : CPointedType<T>(wrap, 0)

public actual abstract class CVariable internal constructor(memory: NativeMemory) : CPointed(memory)
public actual abstract class CVariableType<T : CVariable>(wrap: (NativeMemory) -> T, byteSize: Int) : CPointedType<T>(wrap, byteSize)

public actual abstract class CStructVariable(memory: NativeMemory) : CVariable(memory)
