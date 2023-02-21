package dev.whyoleg.ffi

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class CUIntVariable : CVariable
public expect object CUIntVariableType : CVariableType<CUIntVariable>
public typealias CUInt = UInt

public expect var CUIntVariable.value: CUInt
