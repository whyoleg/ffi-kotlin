package dev.whyoleg.ffi.c

public sealed class CPointed(public val memory: NativeMemory) {
    public abstract val type: Type<*>

    public sealed class Type<T : CPointed> {
        public abstract val layout: NativeLayout
        public abstract fun wrap(memory: NativeMemory): T
        public fun wrap(pointer: NativePointer): T = wrap(NativeMemory(pointer, layout))
    }
}

//TODO: support functions
//public expect class CFunction<T : Function<*>> : CPointed
