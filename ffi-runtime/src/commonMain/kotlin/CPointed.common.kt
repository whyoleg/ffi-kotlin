package dev.whyoleg.ffi

//TODO: abstract vs sealed

public expect abstract class CPointed
public expect abstract class CPointedType<T : CPointed>

//TODO: support functions
//public expect class CFunction<T : Function<*>> : CPointed

public expect abstract class COpaque : CPointed
public expect abstract class COpaqueType<T : COpaque> : CPointedType<T>

public expect abstract class CVariable : CPointed
public expect abstract class CVariableType<T : CVariable> : CPointedType<T>

public expect abstract class CStructVariable : CVariable
