package dev.whyoleg.ffi

public actual class CULongVariable
internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: CPointedType<*> get() = CULongVariableType
}

//TODO: proper long support
public actual object CULongVariableType : CVariableType<CULongVariable>(::CULongVariable, Int.SIZE_BYTES)

public actual var CULongVariable.value: CULong
    get() = memory.loadLong(0).toULong()
    set(value) = run { memory.storeLong(0, value.toLong()) }
