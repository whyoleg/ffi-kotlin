package dev.whyoleg.foreign.tooling.codegen

import dev.whyoleg.foreign.tooling.cbridge.*

//
//expect fun MemoryScope.EVP_MD_fetch(
//    ctx: CPointer<OSSL_LIB_CTX>?,
//    algorithm: CString?,
//    properties: CString?,
//    cleanup: MemoryCleanupAction<CPointer<EVP_MD>?>? = null
//): CPointer<EVP_MD>?
internal fun KotlinCodeBuilder.expectBridgeFunction(
    function: CFunction
) {

}
