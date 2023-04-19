@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*
import kotlinx.cinterop.internal.*

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformUInt,
    scope: ForeignCScope
): OSSL_PARAM = scope.unsafe {
    CGrouped(OSSL_PARAM) { address ->
        ffi_OSSL_PARAM_construct_utf8_string(key.address, buf.address, bsize.toLong(), address)
    }
}

actual fun OSSL_PARAM_construct_end(
    scope: ForeignCScope
): OSSL_PARAM = scope.unsafe {
    CGrouped(OSSL_PARAM) { address ->
        ffi_OSSL_PARAM_construct_end(address)
    }
}

@CCall("ffi_OSSL_PARAM_construct_utf8_string")
private external fun ffi_OSSL_PARAM_construct_utf8_string(key: Long, buf: Long, bsize: Long, returnPointer: Long)

@CCall("ffi_OSSL_PARAM_construct_end")
private external fun ffi_OSSL_PARAM_construct_end(returnPointer: Long)
