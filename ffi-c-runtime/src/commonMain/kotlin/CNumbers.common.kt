package dev.whyoleg.ffi.c

import kotlin.jvm.*

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

public fun CPointer<ByteVariable>.toUByte(): CPointer<UByteVariable> = reinterpret(UByteVariableType)
public fun CPointer<UByteVariable>.toByte(): CPointer<ByteVariable> = reinterpret(ByteVariableType)

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class IntVariable : CVariable
public expect object IntVariableType : CVariableType<IntVariable>

public expect var IntVariable.value: Int

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
