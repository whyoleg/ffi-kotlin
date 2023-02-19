package dev.whyoleg.ffi

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class CULongVariable : CVariable
public expect object CULongVariableType : CVariableType<CULongVariable>
public typealias CULong = ULong

public expect var CULongVariable.value: CULong
