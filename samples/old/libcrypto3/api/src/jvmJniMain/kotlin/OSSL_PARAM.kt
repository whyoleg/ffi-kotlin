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
    CRecord(OSSL_PARAM) { address ->
        osslparam.OSSL_PARAM_construct_utf8_string(key.address, buf.address, bsize.toLong(), address)
    }
}

actual fun OSSL_PARAM_construct_end(
    scope: ForeignCScope
): OSSL_PARAM = scope.unsafe {
    CRecord(OSSL_PARAM) { address ->
        osslparam.OSSL_PARAM_construct_end(address)
    }
}

private object osslparam {
    @JvmStatic
    external fun OSSL_PARAM_construct_utf8_string(
        key: Long, buf: Long, bsize: Long, returnPointer: Long
    )

    @JvmStatic
    external fun OSSL_PARAM_construct_end(returnPointer: Long)
}
