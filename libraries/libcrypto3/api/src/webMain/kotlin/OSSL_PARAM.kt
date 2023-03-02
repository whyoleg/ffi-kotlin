@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.c.*

actual object OSSL_PARAM_Type : CVariableType<OSSL_PARAM>() {
    override val layout: NativeLayout get() = NativeLayout.of(20)
    override fun wrap(memory: NativeMemory): OSSL_PARAM = OSSL_PARAM(memory)
}

actual class OSSL_PARAM(memory: NativeMemory) : CStructVariable(memory) {
    override val type: OSSL_PARAM_Type get() = OSSL_PARAM_Type
}

actual var OSSL_PARAM.key: CString?
    get() = nativeCString(memory.loadInt(0))
    set(value) = memory.storeInt(0, value.nativeAddress)
actual var OSSL_PARAM.data_type: UInt
    get() = memory.loadInt(4).toUInt()
    set(value) = memory.storeInt(4, value.toInt())
actual var OSSL_PARAM.data: CPointer<out CPointed>? //8
    get() = nativeCPointer(COpaqueTypeEmpty, memory.loadInt(8))
    set(value) = memory.storeInt(8, value.nativeAddress)
actual var OSSL_PARAM.data_size: PlatformDependentUInt //12
    get() = memory.loadInt(12).toUInt()
    set(value) = memory.storeInt(12, value.toInt())
actual var OSSL_PARAM.return_size: PlatformDependentUInt //16
    get() = memory.loadInt(16).toUInt()
    set(value) = memory.storeInt(16, value.toInt())
