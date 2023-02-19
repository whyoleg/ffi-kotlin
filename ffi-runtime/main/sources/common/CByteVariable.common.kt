package dev.whyoleg.ffi

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class CByteVariable : CVariable
public expect object CByteVariableType : CVariableType<CByteVariable>
public typealias CByte = Byte

public expect var CByteVariable.value: CByte

public expect fun CPointer<CByteVariable>.toUByte(): CPointer<CUByteVariable>
public expect fun CPointer<CUByteVariable>.toByte(): CPointer<CByteVariable>
