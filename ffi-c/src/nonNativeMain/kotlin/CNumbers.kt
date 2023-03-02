package dev.whyoleg.ffi.c

//Byte
public actual object ByteVariableType : CVariableType<ByteVariable>() {
    override fun wrap(memory: NativeMemory): ByteVariable = ByteVariable(memory)
    override val layout: NativeLayout get() = NativeLayout.Byte
}

public actual class ByteVariable internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: ByteVariableType get() = ByteVariableType
}

public actual var ByteVariable.value: Byte
    get() = memory.loadByte(0)
    set(value) = memory.storeByte(0, value)

//UByte
public actual object UByteVariableType : CVariableType<UByteVariable>() {
    override fun wrap(memory: NativeMemory): UByteVariable = UByteVariable(memory)
    override val layout: NativeLayout get() = NativeLayout.Byte
}

public actual class UByteVariable internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: UByteVariableType get() = UByteVariableType
}

public actual var UByteVariable.value: UByte
    get() = memory.loadByte(0).toUByte()
    set(value) = run { memory.storeByte(0, value.toByte()) }

//Int
public actual object IntVariableType : CVariableType<IntVariable>() {
    override fun wrap(memory: NativeMemory): IntVariable = IntVariable(memory)
    override val layout: NativeLayout get() = NativeLayout.Int
}

public actual class IntVariable internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: IntVariableType get() = IntVariableType
}

public actual var IntVariable.value: Int
    get() = memory.loadInt(0)
    set(value) = run { memory.storeInt(0, value) }

//UInt
public actual object UIntVariableType : CVariableType<UIntVariable>() {
    override fun wrap(memory: NativeMemory): UIntVariable = UIntVariable(memory)
    override val layout: NativeLayout get() = NativeLayout.Int
}

public actual class UIntVariable internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: UIntVariableType get() = UIntVariableType
}

public actual var UIntVariable.value: UInt
    get() = memory.loadInt(0).toUInt()
    set(value) = run { memory.storeInt(0, value.toInt()) }

//Long
public actual object LongVariableType : CVariableType<LongVariable>() {
    override fun wrap(memory: NativeMemory): LongVariable = LongVariable(memory)
    override val layout: NativeLayout get() = NativeLayout.Long
}

public actual class LongVariable internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: LongVariableType get() = LongVariableType
}

public actual var LongVariable.value: Long
    get() = memory.loadLong(0)
    set(value) = run { memory.storeLong(0, value) }

//ULong
public actual class ULongVariable internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: CPointedType<*> get() = ULongVariableType
}

public actual object ULongVariableType : CVariableType<ULongVariable>() {
    override fun wrap(memory: NativeMemory): ULongVariable = ULongVariable(memory)
    override val layout: NativeLayout get() = NativeLayout.Long
}

public actual var ULongVariable.value: ULong
    get() = memory.loadLong(0).toULong()
    set(value) = run { memory.storeLong(0, value.toLong()) }
