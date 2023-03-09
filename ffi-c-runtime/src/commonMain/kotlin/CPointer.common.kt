@file:Suppress("NO_ACTUAL_FOR_EXPECT")

package dev.whyoleg.ffi.c

//TODO: add CArrayPointer.plus/get(index)/set(index)
public typealias CArrayPointer<T> = CPointer<T>
public typealias CArrayPointerVariable<T> = CPointerVariable<T>

public class CPointer<T : CPointed>
internal constructor(
    internal val type: CPointed.Type<T>,
    internal val pointer: NativePointer, //non null
) {
    init {
        require(pointer != NativePointer.NULL) { "CPointer can not have NULL pointer" }
    }

    internal companion object {
        fun <T : CPointed> of(type: CPointed.Type<T>, pointer: NativePointer): CPointer<T>? = when (pointer) {
            NativePointer.NULL -> null
            else               -> CPointer(type, pointer)
        }
    }
}

public class CPointerVariable<T : CPointed>
internal constructor(
    override val type: CPointed.Type<T>,
    memory: NativeMemory,
) : CVariable(memory) {
    public var value: CPointer<T>?
        get() = CPointer.of(type, memory.loadPointer(0))
        set(value) = memory.storePointer(0, value?.pointer ?: NativePointer.NULL)

//    public companion object Type : CVariable.Type<CPointerVariable<*>>
}

@Suppress("UNCHECKED_CAST")
public val <T : CPointed> T.pointer: CPointer<T> get() = CPointer(type, memory.pointer) as CPointer<T>
public val <T : CPointed> CPointer<T>.pointed: T get() = type.wrap(pointer)
public fun <R : CPointed> CPointer<*>.reinterpret(type: CPointed.Type<R>): CPointer<R> = CPointer(type, pointer)
