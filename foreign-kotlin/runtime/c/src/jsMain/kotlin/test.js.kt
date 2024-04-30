import dev.whyoleg.foreign.*
import dev.whyoleg.foreign.c.*

actual fun ERR_get_error(): PlatformUInt {
    return err.ERR_get_error().toUInt()
}

actual fun MemoryScope.ERR_error_string(
    e: PlatformUInt,
    buf: CString?,
    cleanup: MemoryCleanupAction<CString?>?
): CString? {
    val address = err.ERR_error_string(
        e = e.toInt(),
        buf = buf?.run(Unsafe::memoryBlock)?.address ?: 0,
    )
    Unsafe.wrapMemoryBlock(
        address = address,
        layout = MemoryLayout.Address(MemoryLayout.Byte()),
        cleanupAction = cleanup
    ) {
        Unsafe.wrapCPointer(
            mapper = MemoryMapper.Byte(),
            block = it,
            offset = 0
        )
    }
}

@JsModule("foreign-crypto-wasm")
@JsNonModule
@JsName("Module")
private external object err {
    @JsName("_ffi_ERR_get_error")
    fun ERR_get_error(): Int

    @JsName("_ffi_ERR_error_string")
    fun ERR_error_string(e: Int, buf: Int): Int
}

actual fun EVP_DigestSignInit_ex(
    ctx: CPointer<EVP_MD_CTX>?,
    pctx: CPointer<CPointer<EVP_PKEY_CTX>>?,
    mdname: CString?,
    libctx: CPointer<OSSL_LIB_CTX>?,
    props: CString?,
    pkey: CPointer<EVP_PKEY>?,
    params: CArrayPointer<OSSL_PARAM>?,
): Int = with(Unsafe) {
    return evpdigest.EVP_DigestSignInit_ex(
        getBlock(ctx)?.address ?: 0,
        pctx.address,
        mdname.address,
        libctx.address,
        props.address,
        pkey.address,
        params.address,
    )
}

@JsModule("foreign-crypto-wasm")
@JsNonModule
@JsName("Module")
private external object evpdigest {

    @JsName("_ffi_EVP_DigestSignInit_ex")
    fun EVP_DigestSignInit_ex(
        ctx: Int,
        pctx: Int,
        mdname: Int,
        libctx: Int,
        props: Int,
        pkey: Int,
        params: Int,
    ): Int
}

actual fun MemoryScope.OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformUInt,
): OSSL_PARAM = with(Unsafe) {
    // TODO: clash
    val block = allocate(OSSL_PARAM.memoryLayout)
    osslparam.OSSL_PARAM_construct_utf8_string(key.address, buf.address, bsize.toInt(), block.address)
    OSSL_PARAM(block)
}

private class OSSL_PARAM(
    private val block: MemoryBlock
) {
    companion object : CType.Record<OSSL_PARAM> {
    }
}

actual fun OSSL_PARAM_construct_end(
    scope: ForeignCScope
): OSSL_PARAM = scope.unsafe {
    CRecord(OSSL_PARAM) { address ->
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
