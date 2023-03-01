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
    get() = CString(NativePointer(memory.loadLong(0)))
    set(value) = memory.storeLong(0, value.nativePointer)
actual var OSSL_PARAM.data_type: UInt
    get() = memory.loadInt(8).toUInt()
    set(value) = memory.storeInt(8, value.toInt())
actual var OSSL_PARAM.data: CPointer<out CPointed>? // TODO: support opaque pointers and reinterpret
    get() = TODO()
    set(value) = TODO()
actual var OSSL_PARAM.data_size: PlatformDependentUInt
    get() = memory.loadLong(24).toULong()
    set(value) = memory.storeLong(24, value.toLong())
actual var OSSL_PARAM.return_size: PlatformDependentUInt
    get() = memory.loadLong(32).toULong()
    set(value) = memory.storeLong(32, value.toLong())

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformDependentUInt,
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
