package dev.whyoleg.ffi

//TODO: overall should be like this, but it's not possible to make it like this because of compatibility with cinterop
// may be later?
//public expect class ByteVariable : CVariable {
//    public var value: Byte
//
//    public companion object Type : CVariableType<ByteVariable>
//}

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class ByteVariable : CVariable
public expect object ByteVariableType : CVariableType<ByteVariable>

public expect var ByteVariable.value: Byte

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class UByteVariable : CVariable
public expect object UByteVariableType : CVariableType<UByteVariable>

public expect var UByteVariable.value: UByte

public expect fun CPointer<ByteVariable>.toUByte(): CPointer<UByteVariable>
public expect fun CPointer<UByteVariable>.toByte(): CPointer<ByteVariable>

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class UIntVariable : CVariable
public expect object UIntVariableType : CVariableType<UIntVariable>

public expect var UIntVariable.value: UInt

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class LongVariable : CVariable
public expect object LongVariableType : CVariableType<LongVariable>

public expect var LongVariable.value: Long

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class ULongVariable : CVariable
public expect object ULongVariableType : CVariableType<ULongVariable>

public expect var ULongVariable.value: ULong


//TODO: hard to introduce because of current native structure...
//jvm - Long
//wasm/js - Int
//native - Int/Long (mostly Long, but based on target)
//public expect class PlatformInt : Number, Comparable<PlatformInt>
//public expect class PlatformIntVariable : CVariable
//public expect object PlatformIntVariableType : CVariableType<PlatformIntVariable>
//
//public expect var PlatformIntVariable.value: PlatformInt
//
//@JvmInline
//public expect value class PlatformUInt internal constructor(internal val value: PlatformInt) : Comparable<PlatformUInt>
//public expect class PlatformUIntVariable : CVariable
//public expect object PlatformUIntVariableType : CVariableType<PlatformUIntVariable>
//
//public expect var PlatformUIntVariable.value: PlatformUInt
