package dev.whyoleg.foreign.tool.codegen

import dev.whyoleg.foreign.tool.cbridge.api.*


//
//@WasmImport("foreign-crypto-wasm", "ffi_EVP_DigestVerifyUpdate")
//private external fun ffi_EVP_DigestVerifyUpdate(ctx: Int, data: Int, dsize: Int): Int
internal fun KotlinCodeBuilder.wasmExternalFunction(
    function: CFunction
) {

}
