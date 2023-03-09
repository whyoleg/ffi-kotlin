@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection", "PropertyName",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual class OSSL_PARAM(memory: NativeMemory) : CStruct(memory) {
    actual var key: CString?
        get() = nativeCString(memory.loadLong(0))
        set(value) = memory.storeLong(0, value.nativeAddress)
    actual var data_type: UInt
        get() = memory.loadInt(8).toUInt()
        set(value) = memory.storeInt(8, value.toInt())
    actual var data: CPointer<out CPointed>?
        get() = nativeCPointer(COpaqueTypeEmpty, memory.loadLong(16))
        set(value) = memory.storeLong(16, value.nativeAddress)
    actual var data_size: PlatformUInt
        get() = memory.loadLong(24).toULong()
        set(value) = memory.storeLong(24, value.toLong())
    actual var return_size: PlatformUInt
        get() = memory.loadLong(32).toULong()
        set(value) = memory.storeLong(32, value.toLong())

    actual override val type: Type get() = Type

    actual companion object Type : CStruct.Type<OSSL_PARAM>() {
        override val layout: NativeLayout get() = NativeLayout.of(40)
        override fun wrap(memory: NativeMemory): OSSL_PARAM = OSSL_PARAM(memory)
    }
}

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: PlatformUInt,
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
