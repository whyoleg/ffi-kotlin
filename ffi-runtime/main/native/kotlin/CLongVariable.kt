@file:Suppress(
    "ACTUAL_WITHOUT_EXPECT",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi

import kotlinx.cinterop.value as kxvalue

public actual typealias CLongVariable = kotlinx.cinterop.LongVar

@Suppress("DEPRECATION")
public actual object CLongVariableType : CVariableType<CLongVariable>(kotlinx.cinterop.LongVarOf)

public actual inline var CLongVariable.value: CLong
    get() = kxvalue
    set(value) = run { kxvalue = value }
