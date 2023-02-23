package dev.whyoleg.ffi

public actual class CByteVariable(memory: NativeMemory) : CVariable(memory) {
    override val type: CByteVariableType get() = CByteVariableType
}

public actual object CByteVariableType : CVariableType<CByteVariable>(::CByteVariable, 1)

public actual var CByteVariable.value: CByte
    get() = memory.loadByte(0)
    set(value) = memory.storeByte(0, value)

public actual fun CPointer<CByteVariable>.toUByte(): CPointer<CUByteVariable> = CPointer(memory, CUByteVariableType)

public actual fun CPointer<CUByteVariable>.toByte(): CPointer<CByteVariable> = CPointer(memory, CByteVariableType)
