package dev.whyoleg.ffi

public expect abstract class CVariable : CPointed

public expect abstract class CVariableType<T : CVariable> : CPointedType<T>
