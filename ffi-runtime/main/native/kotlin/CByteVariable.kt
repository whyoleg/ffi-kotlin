@file:Suppress(
    "ACTUAL_WITHOUT_EXPECT",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi

import kotlinx.cinterop.reinterpret as kxreinterpret
import kotlinx.cinterop.value as kxvalue

public actual typealias CByteVariable = kotlinx.cinterop.ByteVar

@Suppress("DEPRECATION")
public actual object CByteVariableType : CVariableType<CByteVariable>(kotlinx.cinterop.ByteVarOf)

public actual inline var CByteVariable.value: CByte
    get() = kxvalue
    set(value) = run { kxvalue = value }

public actual fun CPointer<CByteVariable>.toUByte(): CPointer<CUByteVariable> = kxreinterpret()

public actual fun CPointer<CUByteVariable>.toByte(): CPointer<CByteVariable> = kxreinterpret()
