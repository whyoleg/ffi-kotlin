package dev.whyoleg.ffi.c

public actual class CPointer<T : CPointed>
internal constructor(
    internal val type: CPointedType<T>,
    internal val pointer: NativePointer, //non null
) {
    init {
        require(pointer != NativePointer.NULL) { "CPointer can not have NULL pointer" }
    }

    internal companion object {
        fun <T : CPointed> of(type: CPointedType<T>, pointer: NativePointer): CPointer<T>? = when (pointer) {
            NativePointer.NULL -> null
            else               -> CPointer(type, pointer)
        }
    }
}

public actual class CPointerVariable<T : CPointed>
internal constructor(
    override val type: CPointedType<T>,
    memory: NativeMemory,
) : CVariable(memory)

public actual var <T : CPointed> CPointerVariable<T>.value: CPointer<T>?
    get() = CPointer.of(type, memory.loadPointer(0))
    set(value) = memory.storePointer(0, value?.pointer ?: NativePointer.NULL)

@Suppress("UNCHECKED_CAST")
public actual val <T : CPointed> T.pointer: CPointer<T> get() = CPointer(type, memory.pointer) as CPointer<T>
public actual val <T : CPointed> CPointer<T>.pointed: T get() = type.wrap(pointer)
public actual fun <R : CPointed> CPointer<*>.reinterpret(type: CPointedType<R>): CPointer<R> = CPointer(type, pointer)
