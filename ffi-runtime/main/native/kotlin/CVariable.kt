package dev.whyoleg.ffi

public actual typealias CVariable = kotlinx.cinterop.CVariable

@Suppress("DEPRECATION")
public actual abstract class CVariableType<T : CVariable>(
    internal val type: kotlinx.cinterop.CVariable.Type,
) : CPointedType<T>()
