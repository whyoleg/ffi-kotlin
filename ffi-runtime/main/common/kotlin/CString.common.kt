package dev.whyoleg.ffi

public typealias CCharVariable = CByteVariable
public typealias CChar = CByte
public typealias CString = CPointer<CCharVariable>

public expect fun CString.toKString(): String
