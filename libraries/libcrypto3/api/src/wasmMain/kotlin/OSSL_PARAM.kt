@file:Suppress(
    "PrivatePropertyName", "FunctionName", "ClassName", "SpellCheckingInspection",
)

package dev.whyoleg.ffi.libcrypto3

import dev.whyoleg.ffi.*
import kotlin.wasm.*

actual object OSSL_PARAM_Type : CVariableType<OSSL_PARAM>(::OSSL_PARAM, 20/*TODO!!!*/)
actual class OSSL_PARAM(memory: NativeMemory) : CStructVariable(memory) {
    override val type: OSSL_PARAM_Type get() = OSSL_PARAM_Type
}

actual var OSSL_PARAM.key: CString?
    get() = CString(NativePointer(memory.loadLong(0).toInt()))
    set(value) = memory.storeLong(0, value.nativePointer.toLong())
actual var OSSL_PARAM.data_type: UInt
    get() = memory.loadInt(4).toUInt()
    set(value) = memory.storeInt(4, value.toInt())
actual var OSSL_PARAM.data: CPointer<out CPointed>? //8 - TODO: support opaque pointers and reinterpret
    get() = TODO()
    set(value) = TODO()
actual var OSSL_PARAM.data_size: ULong //16
    get() = memory.loadLong(12).toULong()
    set(value) = memory.storeLong(12, value.toLong())
actual var OSSL_PARAM.return_size: ULong //16
    get() = memory.loadLong(16).toULong()
    set(value) = memory.storeLong(16, value.toLong())

actual fun OSSL_PARAM_construct_utf8_string(
    key: CString?,
    buf: CString?,
    bsize: ULong,
): CValue<OSSL_PARAM> = CValue(OSSL_PARAM_Type) { pointer ->
    ffi_OSSL_PARAM_construct_utf8_string(key.nativePointer, buf.nativePointer, bsize.toInt(), pointer.value)
}

actual fun OSSL_PARAM_construct_end(): CValue<OSSL_PARAM> = CValue(OSSL_PARAM_Type) { pointer ->
    ffi_OSSL_PARAM_construct_end(pointer.value)
}

@WasmImport("ffi-libcrypto", "ffi_OSSL_PARAM_construct_utf8_string")
private external fun ffi_OSSL_PARAM_construct_utf8_string(key: Int, buf: Int, bsize: Int, returnPointer: Int)

@WasmImport("ffi-libcrypto", "ffi_OSSL_PARAM_construct_end")
private external fun ffi_OSSL_PARAM_construct_end(returnPointer: Int)

