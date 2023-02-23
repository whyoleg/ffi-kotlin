package dev.whyoleg.ffi

public actual class CLongVariable
internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: CLongVariableType get() = CLongVariableType
}

public actual object CLongVariableType : CVariableType<CLongVariable>(::CLongVariable, 8)

public actual var CLongVariable.value: CLong
    get() = memory.loadLong(0)
    set(value) = run { memory.storeLong(0, value) }
