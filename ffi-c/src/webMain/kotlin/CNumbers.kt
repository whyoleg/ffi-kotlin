package dev.whyoleg.ffi.c

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

//Int
public actual class IntVariable
internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: IntVariableType get() = IntVariableType
}

public actual object IntVariableType : CVariableType<IntVariable>(::IntVariable, Int.SIZE_BYTES)

public actual var IntVariable.value: Int
    get() = memory.loadInt(0)
    set(value) = run { memory.storeInt(0, value) }

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
//TODO: proper long support
public actual class ULongVariable
internal constructor(memory: NativeMemory) : CVariable(memory) {
    override val type: CPointedType<*> get() = ULongVariableType
}

public actual object ULongVariableType : CVariableType<ULongVariable>(::ULongVariable, Long.SIZE_BYTES)

public actual var ULongVariable.value: ULong
    get() = memory.loadLong(0).toULong()
    set(value) = run { memory.storeLong(0, value.toLong()) }

public actual typealias PlatformDependentInt = Int
public actual typealias PlatformDependentIntVariable = IntVariable
public actual typealias PlatformDependentIntVariableType = IntVariableType

public actual typealias PlatformDependentUInt = UInt
public actual typealias PlatformDependentUIntVariable = UIntVariable
public actual typealias PlatformDependentUIntVariableType = UIntVariableType

public actual val Int.pd: PlatformDependentInt get() = this
public actual val UInt.pd: PlatformDependentUInt get() = this

public actual var PlatformDependentIntVariable.pdValue: PlatformDependentInt
    get() = this.value
    set(value) = run { this.value = value }
public actual var PlatformDependentUIntVariable.pdValue: PlatformDependentUInt
    get() = this.value
    set(value) = run { this.value = value }
