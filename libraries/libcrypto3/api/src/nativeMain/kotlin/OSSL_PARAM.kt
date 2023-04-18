@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.memory.*
import dev.whyoleg.foreign.platform.*

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformUInt,
    scope: ForeignCScope
): OSSL_PARAM {
    return dev.whyoleg.ffi.libcrypto3.cinterop.OSSL_PARAM_construct_utf8_string(key, buf, bsize).also {

    }
}

actual fun OSSL_PARAM_construct_end(
    scope: ForeignCScope
): OSSL_PARAM {
    return dev.whyoleg.ffi.libcrypto3.cinterop.OSSL_PARAM_construct_end()
}
