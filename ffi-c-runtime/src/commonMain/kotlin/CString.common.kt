package dev.whyoleg.ffi.c

//TODO!!!

public typealias CCharVariable = CByte
public typealias CChar = Byte
public typealias CString = CPointer<CCharVariable>

public fun CString.toKString(): String = pointed.memory.loadString(0)
