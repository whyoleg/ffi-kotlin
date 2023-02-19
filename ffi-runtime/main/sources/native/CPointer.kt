@file:Suppress(
    "ACTUAL_WITHOUT_EXPECT",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS",
)

package dev.whyoleg.ffi

import kotlinx.cinterop.*

public actual typealias CPointer<T> = kotlinx.cinterop.CPointer<T>

public actual typealias CPointerVariable<T> = kotlinx.cinterop.CPointerVar<T>

public actual val <T : CPointed> T.pointer: CPointer<T>
    get() = ptr

public actual val <T : CPointed> CPointer<T>.pointed: T
    get() = interpretNullablePointed(rawValue)!!
