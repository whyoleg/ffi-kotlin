package dev.whyoleg.ffi

//Byte
public actual class ByteVariable(memory: NativeMemory) : CVariable(memory) {
    override val type: ByteVariableType get() = ByteVariableType
}

public actual object ByteVariableType : CVariableType<ByteVariable>(::ByteVariable, Byte.SIZE_BYTES)

public actual var ByteVariable.value: Byte
    get() = memory.loadByte(0)
    set(value) = memory.storeByte(0, value)

//UByte
public actual class UByteVariable
internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: UByteVariableType get() = UByteVariableType
}

public actual object UByteVariableType : CVariableType<UByteVariable>(::UByteVariable, Byte.SIZE_BYTES)

public actual var UByteVariable.value: UByte
    get() = memory.loadByte(0).toUByte()
    set(value) = run { memory.storeByte(0, value.toByte()) }

public actual fun CPointer<ByteVariable>.toUByte(): CPointer<UByteVariable> = CPointer(memory, UByteVariableType)
public actual fun CPointer<UByteVariable>.toByte(): CPointer<ByteVariable> = CPointer(memory, ByteVariableType)

//UInt
public actual class UIntVariable
internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: UIntVariableType get() = UIntVariableType
}

public actual object UIntVariableType : CVariableType<UIntVariable>(::UIntVariable, Int.SIZE_BYTES)

public actual var UIntVariable.value: UInt
    get() = memory.loadInt(0).toUInt()
    set(value) = run { memory.storeInt(0, value.toInt()) }

//Long
public actual class LongVariable
internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: LongVariableType get() = LongVariableType
}

public actual object LongVariableType : CVariableType<LongVariable>(::LongVariable, Long.SIZE_BYTES)

public actual var LongVariable.value: Long
    get() = memory.loadLong(0)
    set(value) = run { memory.storeLong(0, value) }

//ULong
public actual class ULongVariable
internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: CPointedType<*> get() = ULongVariableType
}

public actual object ULongVariableType : CVariableType<ULongVariable>(::ULongVariable, Long.SIZE_BYTES)

public actual var ULongVariable.value: ULong
    get() = memory.loadLong(0).toULong()
    set(value) = run { memory.storeLong(0, value.toLong()) }
