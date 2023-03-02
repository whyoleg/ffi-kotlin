package dev.whyoleg.ffi.c

public actual abstract class CPointedType<T : CPointed> internal constructor() {
    public abstract val layout: NativeLayout
    public abstract fun wrap(memory: NativeMemory): T
    public fun wrap(pointer: NativePointer): T = wrap(NativeMemory(pointer, layout))
}

public actual abstract class COpaqueType<T : COpaque>() : CPointedType<T>() {
    override val layout: NativeLayout get() = NativeLayout.Empty
}

public object COpaqueTypeEmpty : COpaqueType<COpaque>() {
    override fun wrap(memory: NativeMemory): COpaque = COpaqueImpl(memory)
}

public actual abstract class CVariableType<T : CVariable>() : CPointedType<T>()

public actual abstract class CPointed internal constructor(public val memory: NativeMemory) {
    public abstract val type: CPointedType<*>
}

public actual abstract class COpaque(memory: NativeMemory) : CPointed(memory)
public actual abstract class CVariable internal constructor(memory: NativeMemory) : CPointed(memory)
public actual abstract class CStructVariable(memory: NativeMemory) : CVariable(memory)

private class COpaqueImpl(memory: NativeMemory) : COpaque(memory) {
    override val type: CPointedType<*> get() = COpaqueTypeEmpty
}
