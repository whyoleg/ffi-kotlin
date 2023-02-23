@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*

actual object OSSL_PARAM_Type : CVariableType<OSSL_PARAM>(::OSSL_PARAM, 40/*TODO!!!*/)
actual class OSSL_PARAM(memory: NativeMemory) : CStructVariable(memory) {
    override val type: OSSL_PARAM_Type get() = OSSL_PARAM_Type
}

actual var OSSL_PARAM.key: CString?
    get() = TODO()
    set(value) = TODO()
actual var OSSL_PARAM.data_type: CUInt
    get() = TODO()
    set(value) = TODO()
actual var OSSL_PARAM.data: CPointer<out CPointed>?
    get() = TODO()
    set(value) = TODO()
actual var OSSL_PARAM.data_size: CULong
    get() = TODO()
    set(value) = TODO()
actual var OSSL_PARAM.return_size: CULong
    get() = TODO()
    set(value) = TODO()

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: CULong,
): CValue<OSSL_PARAM> = CValue(OSSL_PARAM_Type) { pointer ->
    osslparam.OSSL_PARAM_construct_utf8_string(key.nativePointer, buf.nativePointer, bsize.toLong(), pointer.value)
}

actual fun OSSL_PARAM_construct_end(): CValue<OSSL_PARAM> = CValue(OSSL_PARAM_Type) { pointer ->
    osslparam.OSSL_PARAM_construct_end(pointer.value)
}

private object osslparam {
    init {
        JNI
    }

    @JvmStatic
    external fun OSSL_PARAM_construct_utf8_string(key: Long, buf: Long, bsize: Long, returnPointer: Long)

    @JvmStatic
    external fun OSSL_PARAM_construct_end(returnPointer: Long)
}
