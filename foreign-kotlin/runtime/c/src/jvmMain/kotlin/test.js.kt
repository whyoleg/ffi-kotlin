import dev.whyoleg.foreign.*
import dev.whyoleg.foreign.MemoryLayout
import dev.whyoleg.foreign.c.*
import java.lang.foreign.*
import java.lang.invoke.*

actual fun ERR_get_error(): PlatformUInt {
    return (err_ffm.ERR_get_error.invokeExact() as Long).toULong()
}

@Suppress("Since15", "InconsistentCommentForJavaParameter")
private object err_ffm {
    private val ERR_get_error: MethodHandle = JdkForeignLinker.methodHandle(
        name = "ERR_get_error",
        descriptor = FunctionDescriptor.of(
            /* returns = */ ValueLayout.JAVA_LONG
        )
    )
    private val ERR_error_string: MethodHandle = JdkForeignLinker.methodHandle(
        name = "ERR_error_string",
        descriptor = FunctionDescriptor.of(
            /* returns = */ ValueLayout.ADDRESS,

            /* e = */ ValueLayout.JAVA_LONG,
            /* buf = */ ValueLayout.ADDRESS
        )
    )

    fun ERR_error_string(
        e: PlatformUInt,
        buf: CString?,
        cleanup: MemoryCleanupAction<CString?>?
    ): CString? {
        val segment = ERR_error_string.invokeExact(
            /* e */ e.toLong(),
            /* buf */ Unsafe.memoryBlockSegment(Unsafe.memoryBlock(buf))
        ) as MemorySegment
        return Unsafe.wrapMemoryBlock(
            segment = segment,
            layout = MemoryLayout.Address(MemoryLayout.Byte()),
            cleanupAction = cleanup,
            wrapper = Unsafe::cString
        )
    }
}

actual fun ERR_error_string(
    e: PlatformUInt,
    buf: CString?,
    cleanup: MemoryCleanupAction<CString?>?
): CString? = when (JdkForeignAvailable) {
    true -> err_ffm.ERR_error_string(e, buf, cleanup)
    else -> err_jni.ERR_error_string(e, buf, cleanup)
}

actual fun MemoryScope.ERR_error_string(
    e: PlatformUInt,
    buf: CString?,
    cleanup: MemoryCleanupAction<CString?>?
): CString? {
    val address = err_ffm.ERR_error_string(
        e = e.toInt(),
        buf = buf?.let(Unsafe::memoryBlock)?.address ?: 0,
    )
    // todo: reduce allocations
    return Unsafe.wrapMemoryBlock(
        address = address,
        layout = MemoryLayout.Address(MemoryLayout.Byte()),
        cleanupAction = cleanup,
        wrapper = Unsafe::cString
    )
}

actual fun MemoryScope.ERR_error_string(
    e: PlatformUInt,
    buf: CPointer<Byte>?,
    cleanup: MemoryCleanupAction<CPointer<Byte>?>?
): CPointer<Byte>? {
    val address = err_ffm.ERR_error_string(
        e = e.toInt(),
        buf = buf?.let(Unsafe::memoryBlock)?.address ?: 0,
    )
    // todo: reduce allocations
    return Unsafe.wrapMemoryBlock(
        address = address,
        layout = MemoryLayout.Address(MemoryLayout.Byte()),
        cleanupAction = cleanup,
        wrapper = Unsafe::cPointer
    )
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
        Unsafe.memoryBlockSegment(ctx?.let(Unsafe::memoryBlock)),
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
