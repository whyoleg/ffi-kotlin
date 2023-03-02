@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual object OSSL_PARAM_Type : CVariableType<OSSL_PARAM>() {
    override val layout: NativeLayout get() = NativeLayout.of(40)

    override fun wrap(memory: NativeMemory): OSSL_PARAM = OSSL_PARAM(memory)
}

actual class OSSL_PARAM(memory: NativeMemory) : CStructVariable(memory) {
    override val type: OSSL_PARAM_Type get() = OSSL_PARAM_Type
}

actual var OSSL_PARAM.key: CString?
    get() = nativeCString(memory.loadLong(0))
    set(value) = memory.storeLong(0, value.nativeAddress)
actual var OSSL_PARAM.data_type: UInt
    get() = memory.loadInt(8).toUInt()
    set(value) = memory.storeInt(8, value.toInt())
actual var OSSL_PARAM.data: CPointer<out CPointed>?
    get() = nativeCPointer(COpaqueTypeEmpty, memory.loadLong(16))
    set(value) = memory.storeLong(16, value.nativeAddress)
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
): CValue<OSSL_PARAM> = nativeCValue(OSSL_PARAM_Type) { nativeAddress ->
    osslparam.OSSL_PARAM_construct_utf8_string(key.nativeAddress, buf.nativeAddress, bsize.toLong(), nativeAddress)
}

actual fun OSSL_PARAM_construct_end(): CValue<OSSL_PARAM> = nativeCValue(OSSL_PARAM_Type) { nativeAddress ->
    osslparam.OSSL_PARAM_construct_end(nativeAddress)
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
