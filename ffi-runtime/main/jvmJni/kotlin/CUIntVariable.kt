package dev.whyoleg.ffi

public actual class CUIntVariable
internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: CUIntVariableType get() = CUIntVariableType
}

public actual object CUIntVariableType : CVariableType<CUIntVariable>(::CUIntVariable, 4)

public actual var CUIntVariable.value: CUInt
    get() = memory.loadInt(0).toUInt()
    set(value) = run { memory.storeInt(0, value.toInt()) }
