package dev.whyoleg.foreign.tool.cbridge.codegen

import dev.whyoleg.foreign.tool.cbridge.api.*

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
