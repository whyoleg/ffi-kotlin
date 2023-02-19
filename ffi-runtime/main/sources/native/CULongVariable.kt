@file:Suppress(
    "ACTUAL_WITHOUT_EXPECT",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi

import kotlinx.cinterop.value as kxvalue

public actual typealias CULongVariable = kotlinx.cinterop.ULongVar

@Suppress("DEPRECATION")
public actual object CULongVariableType : CVariableType<CULongVariable>(kotlinx.cinterop.ULongVarOf)

public actual inline var CULongVariable.value: CULong
    get() = kxvalue
    set(value) = run { kxvalue = value }
