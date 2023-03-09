@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection", "PropertyName",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

expect class OSSL_PARAM : CStruct {
    var key: CString?
    var data_type: UInt
    var data: CPointer<out CPointed>? //COpaque - TODO
    var data_size: PlatformUInt
    var return_size: PlatformUInt

    override val type: Type

    companion object Type : CStruct.Type<OSSL_PARAM>
}

expect fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformUInt,
): CValue<OSSL_PARAM>

expect fun OSSL_PARAM_construct_end(): CValue<OSSL_PARAM>
