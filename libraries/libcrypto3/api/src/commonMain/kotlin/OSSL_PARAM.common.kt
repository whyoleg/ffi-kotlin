@file:Suppress("PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection")

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

expect object OSSL_PARAM_Type : CVariableType<OSSL_PARAM>
expect class OSSL_PARAM : CStructVariable

//Subclasses of NativePointed cannot have properties with backing fields - but there is no backing fields...
expect var OSSL_PARAM.key: CString?
expect var OSSL_PARAM.data_type: UInt
expect var OSSL_PARAM.data: CPointer<out CPointed>? //COpaque - but cinterop is different
expect var OSSL_PARAM.data_size: PlatformDependentUInt
expect var OSSL_PARAM.return_size: PlatformDependentUInt

expect fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformDependentUInt,
): CValue<OSSL_PARAM>

expect fun OSSL_PARAM_construct_end(): CValue<OSSL_PARAM>
