package dev.whyoleg.ffi

public typealias CCharVariable = ByteVariable
public typealias CChar = Byte
public typealias CString = CPointer<CCharVariable>

public expect fun CString.toKString(): String
