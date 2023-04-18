@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformUInt,
    scope: ForeignCScope
): OSSL_PARAM = scope.unsafe {
    CGrouped(OSSL_PARAM) { address ->
        osslparam.OSSL_PARAM_construct_utf8_string(key.address, buf.address, bsize.toInt(), address)
    }
}

actual fun OSSL_PARAM_construct_end(
    scope: ForeignCScope
): OSSL_PARAM = scope.unsafe {
    CGrouped(OSSL_PARAM) { address ->
        osslparam.OSSL_PARAM_construct_end(address)
    }
}

@JsModule("foreign-crypto-wasm")
@JsNonModule
@JsName("Module")
private external object osslparam {
    @JsName("_ffi_OSSL_PARAM_construct_utf8_string")
    fun OSSL_PARAM_construct_utf8_string(key: Int, buf: Int, bsize: Int, returnPointer: Int)

    @JsName("_ffi_OSSL_PARAM_construct_end")
    fun OSSL_PARAM_construct_end(returnPointer: Int)
}
