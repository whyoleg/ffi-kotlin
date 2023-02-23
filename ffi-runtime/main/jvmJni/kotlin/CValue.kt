package dev.whyoleg.ffi

//TODO: should be address, not buffer?
public actual abstract class CValue<T : CVariable>(
    public val type: CVariableType<T>,
    public val memory: NativeMemory,
)

public fun <T : CVariable> CValue(type: CVariableType<T>, block: (pointer: NativePointer) -> Unit): CValue<T> {
    val memory = JNI.autoAllocator.allocate(type.byteSize)
    block(memory.pointer)
    return CValueImpl(type, memory)
}

private class CValueImpl<T : CVariable>(
    type: CVariableType<T>,
    memory: NativeMemory,
) : CValue<T>(type, memory)
