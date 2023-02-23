package dev.whyoleg.ffi

public actual class CULongVariable
internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: CPointedType<*> get() = CULongVariableType
}

public actual object CULongVariableType : CVariableType<CULongVariable>(::CULongVariable, 8)

public actual var CULongVariable.value: CULong
    get() = memory.loadLong(0).toULong()
    set(value) = run { memory.storeLong(0, value.toLong()) }
