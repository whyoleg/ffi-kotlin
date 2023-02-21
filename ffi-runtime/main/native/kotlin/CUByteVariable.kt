@file:Suppress(
    "ACTUAL_WITHOUT_EXPECT",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi

import kotlinx.cinterop.value as kxvalue

public actual typealias CUByteVariable = kotlinx.cinterop.UByteVar

@Suppress("DEPRECATION")
public actual object CUByteVariableType : CVariableType<CUByteVariable>(kotlinx.cinterop.UByteVarOf)

public actual inline var CUByteVariable.value: CUByte
    get() = kxvalue
    set(value) = run { kxvalue = value }
