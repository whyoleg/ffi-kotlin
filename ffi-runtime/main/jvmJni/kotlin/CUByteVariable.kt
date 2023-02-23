package dev.whyoleg.ffi

public actual class CUByteVariable
internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: CUByteVariableType get() = CUByteVariableType
}

public actual object CUByteVariableType : CVariableType<CUByteVariable>(::CUByteVariable, 1)

public actual var CUByteVariable.value: CUByte
    get() = memory.loadByte(0).toUByte()
    set(value) = run { memory.storeByte(0, value.toByte()) }
