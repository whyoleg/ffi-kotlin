package dev.whyoleg.ffi

@Suppress("NO_ACTUAL_FOR_EXPECT")
public expect class CLongVariable : CVariable
public expect object CLongVariableType : CVariableType<CLongVariable>
public typealias CLong = Long

public expect var CLongVariable.value: CLong
