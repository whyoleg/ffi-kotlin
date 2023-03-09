package dev.whyoleg.ffi.c

//TODO: better name?
public sealed class CVariable(memory: NativeMemory) : CPointed(memory) {
    public sealed class Type<T : CVariable> : CPointed.Type<T>()
}

public abstract class CStruct(memory: NativeMemory) : CVariable(memory) {
    public abstract class Type<T : CStruct> : CVariable.Type<T>()
}

public abstract class CUnion(memory: NativeMemory) : CVariable(memory) {
    public abstract class Type<T : CUnion> : CVariable.Type<T>()
}

public abstract class COpaque(memory: NativeMemory) : CVariable(memory) {
    public abstract class Type<T : COpaque> : CVariable.Type<T>() {
        final override val layout: NativeLayout get() = NativeLayout.Empty
    }
}
