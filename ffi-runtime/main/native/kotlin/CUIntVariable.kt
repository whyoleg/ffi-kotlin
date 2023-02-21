@file:Suppress(
    "ACTUAL_WITHOUT_EXPECT",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi

import kotlinx.cinterop.value as kxvalue

public actual typealias CUIntVariable = kotlinx.cinterop.UIntVar

@Suppress("DEPRECATION")
public actual object CUIntVariableType : CVariableType<CUIntVariable>(kotlinx.cinterop.UIntVarOf)

public actual inline var CUIntVariable.value: CUInt
    get() = kxvalue
    set(value) = run { kxvalue = value }
