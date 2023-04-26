@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.foreign.c.*
import dev.whyoleg.foreign.platform.*
import kotlin.wasm.*

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformUInt,
    scope: ForeignCScope
): OSSL_PARAM = scope.unsafe {
    CRecord(OSSL_PARAM) { address ->
        ffi_OSSL_PARAM_construct_utf8_string(key.address, buf.address, bsize.toInt(), address)
    }
}

actual fun OSSL_PARAM_construct_end(
    scope: ForeignCScope
): OSSL_PARAM = scope.unsafe {
    CRecord(OSSL_PARAM) { address ->
        ffi_OSSL_PARAM_construct_end(address)
    }
}

@WasmImport("foreign-crypto-wasm", "ffi_OSSL_PARAM_construct_utf8_string")
private external fun ffi_OSSL_PARAM_construct_utf8_string(key: Int, buf: Int, bsize: Int, returnPointer: Int)

@WasmImport("foreign-crypto-wasm", "ffi_OSSL_PARAM_construct_end")
private external fun ffi_OSSL_PARAM_construct_end(returnPointer: Int)
