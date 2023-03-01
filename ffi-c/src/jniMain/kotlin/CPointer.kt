package dev.whyoleg.ffi.c

public actual class CPointer<T : CPointed>
@PublishedApi
internal constructor(
    internal val memory: NativeMemory,
    internal val type: CPointedType<T>,
)

public actual class CPointerVariable<T : CPointed>
internal constructor(
    memory: NativeMemory,
    override val type: CPointedType<T>,
) : CVariable(memory)

public actual var <T : CPointed> CPointerVariable<T>.value: CPointer<T>?
    get() = CPointer(NativePointer(memory.loadLong(0)), type)
    set(value) = run { memory.storeLong(0, value?.memory?.pointer?.value ?: 0) }

@Suppress("UNCHECKED_CAST")
public actual val <T : CPointed> T.pointer: CPointer<T>
    get() = CPointer(memory, type) as CPointer<T>

public actual val <T : CPointed> CPointer<T>.pointed: T
    get() = type.wrap(memory)

//change args order
public fun <T : CPointed> CPointer(pointer: NativePointer, type: CPointedType<T>): CPointer<T>? {
    return NativeMemory(pointer, 0)?.let { CPointer(it, type) }
}

public val CPointer<*>?.nativePointer: Long get() = this?.memory?.pointer?.value ?: 0
