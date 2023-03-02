@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformDependentUInt,
): CValue<OSSL_PARAM> = nativeCValue(OSSL_PARAM_Type) { nativeAddress ->
    osslparam.OSSL_PARAM_construct_utf8_string(key.nativeAddress, buf.nativeAddress, bsize.toInt(), nativeAddress)
}

actual fun OSSL_PARAM_construct_end(): CValue<OSSL_PARAM> = nativeCValue(OSSL_PARAM_Type) { nativeAddress ->
    osslparam.OSSL_PARAM_construct_end(nativeAddress)
}

@JsModule("ffi-libcrypto")
@JsNonModule
@JsName("Module")
private external object osslparam {
    @JsName("_ffi_OSSL_PARAM_construct_utf8_string")
    fun OSSL_PARAM_construct_utf8_string(key: Int, buf: Int, bsize: Int, returnPointer: Int)

    @JsName("_ffi_OSSL_PARAM_construct_end")
    fun OSSL_PARAM_construct_end(returnPointer: Int)
}
