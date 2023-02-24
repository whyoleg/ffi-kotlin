package dev.whyoleg.ffi

public actual abstract class COpaque(memory: NativeMemory) : CPointed(memory)
public actual abstract class COpaqueType<T : COpaque>(
    wrap: (NativeMemory) -> T,
) : CPointedType<T>(wrap, 0)

internal class COpaqueImpl(memory: NativeMemory) : COpaque(memory) {
    override val type: CPointedType<*>
        get() = TODO("Not yet implemented")
}
