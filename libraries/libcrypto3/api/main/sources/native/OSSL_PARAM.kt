@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
    "ACTUAL_TYPE_ALIAS_NOT_TO_CLASS", "EXTENSION_SHADOWED_BY_MEMBER",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual object OSSL_PARAM_Type : CVariableType<OSSL_PARAM>(OSSL_PARAM)
actual typealias OSSL_PARAM = dev.whyoleg.ffi.libcrypto3.cinterop.OSSL_PARAM

actual var OSSL_PARAM.key: CString?
    get() = key
    set(value) = run { key = value }
actual var OSSL_PARAM.data_type: CUInt
    get() = data_type
    set(value) = run { data_type = value }
actual var OSSL_PARAM.data: CPointer<out CPointed>?
    get() = data
    set(value) = run { data = value }
actual var OSSL_PARAM.data_size: CULong
    get() = data_size
    set(value) = run { data_size = value }
actual var OSSL_PARAM.return_size: CULong
    get() = return_size
    set(value) = run { return_size = value }
