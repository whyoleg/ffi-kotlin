package dev.whyoleg.ffi

public actual abstract class CPointed
internal constructor(
    public val memory: NativeMemory,
) {
    public abstract val type: CPointedType<*>
}

public actual abstract class CPointedType<T : CPointed>(
    internal val wrap: (NativeMemory) -> T,
    internal val byteSize: Int,
)
