package dev.whyoleg.foreign.tooling.codegen

import dev.whyoleg.foreign.tooling.cbridge.*


//
//@WasmImport("foreign-crypto-wasm", "ffi_EVP_DigestVerifyUpdate")
//private external fun ffi_EVP_DigestVerifyUpdate(ctx: Int, data: Int, dsize: Int): Int
internal fun KotlinCodeBuilder.wasmExternalFunction(
    function: CFunction
) {

}
