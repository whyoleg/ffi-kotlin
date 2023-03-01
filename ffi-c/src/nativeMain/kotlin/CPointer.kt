@file:Suppress(
    "ACTUAL_WITHOUT_EXPECT",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi.c

import kotlinx.cinterop.*
import kotlinx.cinterop.value as kxvalue

public actual typealias CPointer<T> = kotlinx.cinterop.CPointer<T>

public actual typealias CPointerVariable<T> = kotlinx.cinterop.CPointerVar<T>

public actual var <T : CPointed> CPointerVariable<T>.value: CPointer<T>?
    get() = kxvalue
    set(value) = run { kxvalue = value }

public actual val <T : CPointed> T.pointer: CPointer<T>
    get() = ptr

public actual val <T : CPointed> CPointer<T>.pointed: T
    get() = interpretNullablePointed(rawValue)!!
