package dev.whyoleg.ffi

import java.lang.foreign.*

//Byte
public actual class ByteVariable internal constructor(segment: MemorySegment) : CVariable(segment)

public actual object ByteVariableType : CVariableType<ByteVariable>(::ByteVariable, ValueLayout.JAVA_BYTE)

public actual var ByteVariable.value: Byte
    get() = segment.get(ValueLayout.JAVA_BYTE, 0)
    set(value) = segment.set(ValueLayout.JAVA_BYTE, 0, value)

//UByte
public actual class UByteVariable internal constructor(segment: MemorySegment) : CVariable(segment)

public actual object UByteVariableType : CVariableType<UByteVariable>(::UByteVariable, ValueLayout.JAVA_BYTE)

public actual var UByteVariable.value: UByte
    get() = segment.get(ValueLayout.JAVA_BYTE, 0).toUByte()
    set(value) = segment.set(ValueLayout.JAVA_BYTE, 0, value.toByte())

public actual fun CPointer<ByteVariable>.toUByte(): CPointer<UByteVariable> = CPointer(UByteVariable(pointed.segment))

public actual fun CPointer<UByteVariable>.toByte(): CPointer<ByteVariable> = CPointer(ByteVariable(pointed.segment))

//UInt
public actual class UIntVariable internal constructor(segment: MemorySegment) : CVariable(segment)

public actual object UIntVariableType : CVariableType<UIntVariable>(::UIntVariable, ValueLayout.JAVA_INT)

public actual var UIntVariable.value: UInt
    get() = segment.get(ValueLayout.JAVA_INT, 0).toUInt()
    set(value) = segment.set(ValueLayout.JAVA_INT, 0, value.toInt())

//Long
public actual class LongVariable internal constructor(segment: MemorySegment) : CVariable(segment)

public actual object LongVariableType : CVariableType<LongVariable>(::LongVariable, ValueLayout.JAVA_LONG)

public actual var LongVariable.value: Long
    get() = segment.get(ValueLayout.JAVA_LONG, 0)
    set(value) = segment.set(ValueLayout.JAVA_LONG, 0, value)

//ULong
public actual class ULongVariable internal constructor(segment: MemorySegment) : CVariable(segment)

public actual object ULongVariableType : CVariableType<ULongVariable>(::ULongVariable, ValueLayout.JAVA_LONG)

public actual var ULongVariable.value: ULong
    get() = segment.get(ValueLayout.JAVA_LONG, 0).toULong()
    set(value) = segment.set(ValueLayout.JAVA_LONG, 0, value.toLong())
