package dev.whyoleg.ffi

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class CUByteVariable : CVariable
public expect object CUByteVariableType : CVariableType<CUByteVariable>
public typealias CUByte = UByte

public expect var CUByteVariable.value: CUByte
