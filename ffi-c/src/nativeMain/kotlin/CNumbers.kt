@file:Suppress(
    "ACTUAL_WITHOUT_EXPECT",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
    "DEPRECATION"
)

package dev.whyoleg.ffi.c

import kotlinx.cinterop.reinterpret as kxreinterpret
import kotlinx.cinterop.value as kxvalue

//Byte
public actual typealias ByteVariable = kotlinx.cinterop.ByteVar

public actual object ByteVariableType : CVariableType<ByteVariable>(kotlinx.cinterop.ByteVarOf)

public actual inline var ByteVariable.value: Byte
    get() = kxvalue
    set(value) = run { kxvalue = value }

//UByte
public actual typealias UByteVariable = kotlinx.cinterop.UByteVar

public actual object UByteVariableType : CVariableType<UByteVariable>(kotlinx.cinterop.UByteVarOf)

public actual inline var UByteVariable.value: UByte
    get() = kxvalue
    set(value) = run { kxvalue = value }

public actual fun CPointer<ByteVariable>.toUByte(): CPointer<UByteVariable> = kxreinterpret()

public actual fun CPointer<UByteVariable>.toByte(): CPointer<ByteVariable> = kxreinterpret()

//Int
public actual typealias IntVariable = kotlinx.cinterop.IntVar

public actual object IntVariableType : CVariableType<IntVariable>(kotlinx.cinterop.IntVarOf)

public actual inline var IntVariable.value: Int
    get() = kxvalue
    set(value) = run { kxvalue = value }

//UInt
public actual typealias UIntVariable = kotlinx.cinterop.UIntVar

public actual object UIntVariableType : CVariableType<UIntVariable>(kotlinx.cinterop.UIntVarOf)

public actual inline var UIntVariable.value: UInt
    get() = kxvalue
    set(value) = run { kxvalue = value }

//Long
public actual typealias LongVariable = kotlinx.cinterop.LongVar

public actual object LongVariableType : CVariableType<LongVariable>(kotlinx.cinterop.LongVarOf)

public actual inline var LongVariable.value: Long
    get() = kxvalue
    set(value) = run { kxvalue = value }

//Ulong
public actual typealias ULongVariable = kotlinx.cinterop.ULongVar

public actual object ULongVariableType : CVariableType<ULongVariable>(kotlinx.cinterop.ULongVarOf)

public actual inline var ULongVariable.value: ULong
    get() = kxvalue
    set(value) = run { kxvalue = value }
