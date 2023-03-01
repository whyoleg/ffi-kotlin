package dev.whyoleg.ffi.c

public actual typealias CPointed = kotlinx.cinterop.CPointed

public actual abstract class CPointedType<T : CPointed>

public actual typealias COpaque = kotlinx.cinterop.COpaque

public actual abstract class COpaqueType<T : COpaque> : CPointedType<T>()

public actual typealias CVariable = kotlinx.cinterop.CVariable

@Suppress("DEPRECATION")
public actual abstract class CVariableType<T : CVariable>(internal val type: kotlinx.cinterop.CVariable.Type) : CPointedType<T>()

public actual typealias CStructVariable = kotlinx.cinterop.CStructVar
